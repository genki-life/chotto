package team.genki.chotto

import com.github.fluidsonic.fluid.json.*
import io.ktor.application.ApplicationCall
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.ApplicationFeature
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.request.ApplicationReceivePipeline
import io.ktor.request.ApplicationReceiveRequest
import io.ktor.request.contentCharset
import io.ktor.request.contentType
import io.ktor.request.httpMethod
import io.ktor.util.AttributeKey
import io.ktor.util.pipeline.PipelineContext
import io.ktor.util.toMap
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.io.jvm.javaio.toInputStream
import java.nio.charset.Charset


internal class ChottoCommandRequestFeature<Transaction : ChottoTransaction>(
	private val jsonCodecProvider: JSONCodecProvider<Transaction>
) : ApplicationFeature<ApplicationCallPipeline, Unit, Unit> {

	override val key = AttributeKey<Unit>("Chotto: command request feature")


	private fun PipelineContext<ApplicationReceiveRequest, ApplicationCall>.resolveBody(factory: ChottoCommandFactory<Transaction, *, *>): ByteReadChannel {
		val contentType = call.request.contentType().withoutParameters()

		if (contentType.match(ContentType.Application.Json))
			return call.request.receiveChannel()

		if (methodsAllowedForQueryParameterBody.contains(call.request.httpMethod))
			call.request.queryParameters["body"]?.let { return ByteReadChannel(it, Charset.defaultCharset()) }

		if (contentType.match(ContentType.Any) && (factory is ChottoCommandFactory.Empty<*, *, *> || !call.parameters.isEmpty()))
			ByteReadChannel(text = "{}", charset = Charsets.UTF_8)

		throw ChottoCommandFailure(
			code = "invalidRequest",
			developerMessage = "Expected content of type '${ContentType.Application.Json}'",
			userMessage = ChottoCommandFailure.genericUserMessage
		)
	}


	@Suppress("UNCHECKED_CAST")
	override fun install(pipeline: ApplicationCallPipeline, configure: Unit.() -> Unit) {
		Unit.configure()

		pipeline.receivePipeline.intercept(ApplicationReceivePipeline.Transform) { subject ->
			val factory = subject.value as? ChottoCommandFactory<Transaction, *, *>
				?: throw ChottoCommandFailure(code = "fixme", developerMessage = "FIXME", userMessage = ChottoCommandFailure.genericUserMessage) // FIXME

			val transaction = transaction as Transaction
			val body = resolveBody(factory = factory)

			proceedWith(ApplicationReceiveRequest(
				type = subject.type,
				value = parseCommandRequest(
					transaction = transaction,
					body = body,
					charset = call.request.contentCharset() ?: Charsets.UTF_8,
					parameters = call.parameters,
					factory = factory
				)
			))
		}
	}


	private fun parseCommandRequest(
		transaction: Transaction,
		body: ByteReadChannel,
		charset: Charset,
		parameters: Parameters,
		factory: ChottoCommandFactory<Transaction, *, *>
	): ChottoCommandRequest {
		val reader = JSONReader.build(body.toInputStream().reader(charset = charset)) // FIXME blocking

		try {
			var command: ChottoCommand? = null

			reader.readFromMapByElementValue { key ->
				when (key) {
					"command" -> command = readCommand(
						factory = factory,
						parameters = parameters,
						transaction = transaction
					)
					else -> skipValue()
				}
			}

			if (command == null) {
				command = if (factory is ChottoCommandFactory.Empty<Transaction, *, *>) {
					factory.createCommand()
				}
				else {
					JSONReader.build("""{"command":{}}""").run {
						readFromMap {
							readMapKey()
							readCommand(
								factory = factory,
								parameters = parameters,
								transaction = transaction
							)
						}
					}
				}
			}

			return ChottoCommandRequest(
				command = command!!
			)
		}
		catch (e: JSONException) {
			if (e is JSONException.Schema || e is JSONException.Syntax) {
				throw ChottoCommandFailure(
					code = "invalidRequest",
					developerMessage = e.message,
					userMessage = ChottoCommandFailure.genericUserMessage,
					cause = e
				)
			}

			throw e
		}
	}


	private fun JSONReader.readCommand(
		factory: ChottoCommandFactory<Transaction, *, *>,
		parameters: Parameters,
		transaction: Transaction
	): ChottoCommand {
		var commandReader = this
		if (!parameters.isEmpty()) {
			commandReader = PropertyInjectingJSONReader(
				properties = parameters.toMap().mapValues { it.value.single() },
				source = commandReader
			)
		}

		val decoder = JSONDecoder.builder(transaction)
			.codecs(jsonCodecProvider)
			.source(commandReader)
			.build()

		return try {
			factory.run { decoder.decodeCommand() }
		}
		catch (e: JSONException) {
			e.addSuppressed(JSONException.Parsing("… when decoding command '${factory.name}' using ${factory::class.qualifiedName}"))
			throw e
		}
	}


	companion object {

		private val methodsAllowedForQueryParameterBody = setOf(HttpMethod.Get, HttpMethod.Head)
	}
}
