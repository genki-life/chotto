package team.genki.chotto.server

import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.interfaces.Payload
import com.github.fluidsonic.fluid.mongo.*
import com.github.fluidsonic.fluid.stdlib.*
import io.ktor.application.Application
import io.ktor.application.ApplicationStarting
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Principal
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.auth.jwt.jwt
import io.ktor.client.utils.CacheControl
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.features.XForwardedHeaderSupport
import io.ktor.http.HttpHeaders
import io.ktor.request.ApplicationReceiveRequest
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.runBlocking
import org.bson.codecs.configuration.CodecConfigurationException
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.slf4j.event.Level
import team.genki.chotto.core.*


class ChottoServer internal constructor() {

	class Builder<Context : ChottoServerContext, Transaction : ChottoTransaction> internal constructor() {

		private var endpoints: MutableMap<String, Endpoint> = mutableMapOf()
		private var environment: ChottoEnvironment<Context, Transaction>? = null
		private val ktorEnvironment = commandLineEnvironment(arrayOf())
		private var modules: List<ChottoModule<in Context, in Transaction>>? = null
		private val providerBasedBsonCodecRegistry = ProviderBasedBsonCodecRegistry<Context>()

		val bsonCodecRegistry = CodecRegistries.fromRegistries(
			MongoClients.defaultCodecRegistry,
			providerBasedBsonCodecRegistry
		)!!

		val config = ktorEnvironment.config


		fun authenticator(jwtRealm: String, jwtVerifier: JWTVerifier, authenticate: suspend Transaction.(jwtPayload: Payload) -> Boolean) =
			object : Authenticator<Transaction> {

				override val jwtRealm = jwtRealm
				override val jwtVerifier = jwtVerifier

				override suspend fun Transaction.authenticate(jwtPayload: Payload) =
					authenticate(jwtPayload)
			}


		internal fun build(): ChottoServer {
			// create engine before monitoring the start event because Netty's subscriptions must be processed first
			val engine = embeddedServer(Netty, ktorEnvironment)
			val subscription = ktorEnvironment.monitor.subscribe(ApplicationStarting) { application ->
				application.apply {
					configureBasics()
					configureModules()
				}
			}

			engine.start()
			subscription.dispose()

			return ChottoServer()
		}


		internal fun configure(assemble: suspend Builder<Context, Transaction>.() -> ChottoEnvironment<Context, Transaction>) {
			runBlocking {
				environment = assemble()
			}
		}


		fun <TCommandRequestMeta : CommandRequest.Meta, TCommandResponseMeta : CommandResponse.Meta> endpoint(
			model: ClientModel<TCommandRequestMeta, TCommandResponseMeta>,
			responseMetaFactory: suspend Transaction.(command: Command) -> TCommandResponseMeta,
			authenticator: Authenticator<Transaction>? = null
		) {
			when (val existingModel = endpoints[model.name]) {
				null -> Unit
				model -> error("Cannot create multiple endpoints with model ${model::class.qualifiedName} (${model.name})")
				else -> error("Cannot create multiple endpoints with same model name '${model.name}' (${model::class.qualifiedName} and ${existingModel::class.qualifiedName})")
			}

			endpoints[model.name] = Endpoint(
				authenticator = authenticator,
				model = model,
				responseMetaFactory = responseMetaFactory
			)
		}


		fun modules(vararg modules: ChottoModule<in Context, in Transaction>) {
			check(this.modules == null) { "modules() can only be specified once" }

			this.modules = modules.toList()
		}


		private fun Application.configureBasics() {
			install(CallLogging) {
				level = Level.INFO
			}

			install(DefaultHeaders) {
				header(HttpHeaders.CacheControl, CacheControl.NO_STORE)
				header(HttpHeaders.Pragma, "no-cache")
				header(HttpHeaders.Server, "Chotto")
			}

			install(CORS) {
				anyHost()
				exposeHeader(HttpHeaders.WWWAuthenticate)
				header(HttpHeaders.Accept) // https://github.com/ktorio/ktor/issues/939
				header(HttpHeaders.AcceptLanguage) // https://github.com/ktorio/ktor/issues/939
				header(HttpHeaders.Authorization)
			}

			install(XForwardedHeaderSupport)
			install(EncryptionEnforcementFeature)
		}


		private fun Application.configureModules() {
			val modules = (modules ?: error("modules() must be specified")) + StandardModule
			val environment = this@Builder.environment!!

			val context = runBlocking { environment.createContext() }
			val contextConfigurations = modules.map { it.configurationForContext(context) }

			val entityTypes = contextConfigurations.flatMap { it.ids.types }

			val bsonCodecProviders: MutableList<BsonCodecProvider<Context>> = mutableListOf()
			bsonCodecProviders += contextConfigurations.flatMap { it.bson.codecProviders }
			bsonCodecProviders += entityTypes.map(::SpecificEntityIdBsonCodec)
			bsonCodecProviders += TypedIdBsonCodec(types = entityTypes)

			providerBasedBsonCodecRegistry.context = context
			providerBasedBsonCodecRegistry.provider = BsonCodecProvider(bsonCodecProviders)
			providerBasedBsonCodecRegistry.rootRegistry = bsonCodecRegistry

			val configuration = ServerConfiguration(
				context = context,
				environment = environment,
				modules = modules
			)

			install(TransactionFeature(configuration))
			install(CommandFailureFeature)
			install(CommandRequestFeature)
			install(CommandResponseFeature)

			if (endpoints.isEmpty())
				error("at least one endpoint() must be specified")

			// TODO check for model name collisions (or rename endpoints to models)

			authentication {
				for (endpoint in endpoints.values) {
					endpoint.authenticator?.let { authenticator ->
						jwt(name = endpoint.model.name) {
							realm = authenticator.jwtRealm
							verifier(authenticator.jwtVerifier)

							validate { credential ->
								with(authenticator) {
									val transaction = chottoCall.transaction as Transaction
									transaction.authenticate(credential.payload)
										.thenTake { object : Principal {} }
								}
							}
						}
					}
				}
			}

			routing {
				for (endpoint in endpoints.values) {
					authenticate(endpoint.model.name, optional = true) {
						post(endpoint.model.name) {
							val request = call.request.pipeline.execute(call, ApplicationReceiveRequest(
								type = CommandRequest::class,
								value = CommandRequestPipelineData(
									model = endpoint.model
								)
							)).value as CommandRequest<*, *>

							val chottoCall = chottoCall

							@Suppress("UNCHECKED_CAST")
							val transaction = this.chottoCall.transaction as Transaction

							val result = chottoCall.commandHandler.handle(request.command)
							val meta = endpoint.responseMetaFactory(transaction, request.command)

							call.respond(CommandResponsePipelineData(
								meta = meta,
								model = endpoint.model,
								result = result
							))
						}
					}
				}
			}

			runBlocking {
				environment.onStart(context = context)
			}
		}


		private inner class Endpoint(
			val authenticator: Authenticator<Transaction>?,
			val model: ClientModel<*, *>,
			val responseMetaFactory: suspend Transaction.(Command) -> CommandResponse.Meta
		)
	}


	private class ProviderBasedBsonCodecRegistry<Context : ChottoServerContext> : CodecRegistry {

		lateinit var context: Context
		lateinit var provider: BsonCodecProvider<Context>
		lateinit var rootRegistry: CodecRegistry


		override fun <Value : Any> get(clazz: Class<Value>): BsonCodec<Value, Context> {
			val codec = provider.codecForClass(clazz.kotlin) ?: throw CodecConfigurationException("No BSON codec provided for $clazz")
			if (codec is AbstractBsonCodec<Value, Context>) {
				codec.configure(context = context, rootRegistry = rootRegistry)
			}

			return codec
		}
	}
}


fun <Context : ChottoServerContext, Transaction : ChottoTransaction> chottoServer(
	assemble: suspend ChottoServer.Builder<Context, Transaction>.() -> ChottoEnvironment<Context, Transaction>
) {
	ChottoServer.Builder<Context, Transaction>().apply { configure(assemble) }.build()
}
