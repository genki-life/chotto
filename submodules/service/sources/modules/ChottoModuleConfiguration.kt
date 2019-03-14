package team.genki.chotto

import com.github.fluidsonic.fluid.json.JSONCodecProvider
import com.github.fluidsonic.fluid.json.JSONEncoder
import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.routing.Route
import io.ktor.routing.method
import io.ktor.routing.route
import io.ktor.util.pipeline.ContextDsl
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.reflect.KClass


class ChottoModuleConfiguration<Context : ChottoContext, Transaction : ChottoTransaction> internal constructor(
	internal val module: ChottoModule<Context, Transaction>
) {

	@PublishedApi
	internal val commands = Commands()

	@PublishedApi
	internal val entities = Entities()

	internal val additionalResponseEncodings: MutableList<JSONEncoder<Transaction>.() -> Unit> = mutableListOf()
	internal val bsonCodecProviders: MutableList<BSONCodecProvider<Context>> = mutableListOf()
	internal val commandRoutes: MutableList<ChottoCommandRoute<Transaction>> = mutableListOf()
	internal val customConfigurations: MutableList<Application.() -> Unit> = mutableListOf()
	internal val idFactories: MutableSet<EntityId.Factory<*>> = mutableSetOf()
	internal val jsonCodecProviders: MutableList<JSONCodecProvider<Transaction>> = mutableListOf()
	internal val routeConfigurations: MutableList<Route.() -> Unit> = mutableListOf()
	internal val routeWrappers: MutableList<Route.(next: Route.() -> Unit) -> Route> = mutableListOf()
	internal val routedCommandNames: HashSet<ChottoCommandName> = hashSetOf()


	fun bson(vararg providers: BSONCodecProvider<Context>) {
		bsonCodecProviders += providers
	}


	inline fun commands(configure: Commands.() -> Unit) {
		commands.configure()
	}


	fun custom(configure: Application.() -> Unit) {
		customConfigurations += configure
	}


	inline fun entities(configure: Entities.() -> Unit) {
		entities.configure()
	}


	fun ids(vararg factories: EntityId.Factory<*>) {
		idFactories += factories
	}


	fun json(vararg providers: JSONCodecProvider<Transaction>) {
		jsonCodecProviders += providers
	}


	fun additionalResponseEncoding(encode: JSONEncoder<Transaction>.() -> Unit) {
		additionalResponseEncodings += encode
	}


	@ContextDsl
	fun routes(configure: Route.() -> Unit) {
		routeConfigurations += configure
	}


	fun wrapAllRoutes(wrapper: Route.(next: Route.() -> Unit) -> Route) {
		routeWrappers += wrapper
	}


	private fun Route.addRoute(method: HttpMethod, commandFactory: ChottoCommandFactory<Transaction, *, *>) {
		method(method) {
			handle(commandFactory = commandFactory)
		}
	}


	private fun Route.addRoute(method: HttpMethod, path: String, commandFactory: ChottoCommandFactory<Transaction, *, *>) =
		route(path) {
			addRoute(method = method, commandFactory = commandFactory)
		}


	@ContextDsl
	fun Route.delete(path: String, commandFactory: ChottoCommandFactory<Transaction, *, *>) =
		addRoute(method = HttpMethod.Delete, path = path, commandFactory = commandFactory)


	@ContextDsl
	fun Route.delete(commandFactory: ChottoCommandFactory<Transaction, *, *>) =
		addRoute(method = HttpMethod.Delete, commandFactory = commandFactory)


	@ContextDsl
	fun Route.get(path: String, commandFactory: ChottoCommandFactory<Transaction, *, *>) =
		addRoute(method = HttpMethod.Get, path = path, commandFactory = commandFactory)


	@ContextDsl
	fun Route.get(commandFactory: ChottoCommandFactory<Transaction, *, *>) =
		addRoute(method = HttpMethod.Get, commandFactory = commandFactory)


	@ContextDsl
	fun Route.head(path: String, commandFactory: ChottoCommandFactory<Transaction, *, *>) =
		addRoute(method = HttpMethod.Head, path = path, commandFactory = commandFactory)


	@ContextDsl
	fun Route.head(commandFactory: ChottoCommandFactory<Transaction, *, *>) =
		addRoute(method = HttpMethod.Head, commandFactory = commandFactory)


	@ContextDsl
	fun Route.post(path: String, commandFactory: ChottoCommandFactory<Transaction, *, *>) =
		addRoute(method = HttpMethod.Post, path = path, commandFactory = commandFactory)


	@ContextDsl
	fun Route.post(commandFactory: ChottoCommandFactory<Transaction, *, *>) =
		addRoute(method = HttpMethod.Post, commandFactory = commandFactory)


	@ContextDsl
	private fun Route.handle(commandFactory: ChottoCommandFactory<Transaction, *, *>) {
		commandRoutes += ChottoCommandRoute(
			factory = commandFactory,
			route = this
		)
	}


	@ContextDsl
	fun Route.options(path: String, commandFactory: ChottoCommandFactory<Transaction, *, *>) =
		addRoute(method = HttpMethod.Options, path = path, commandFactory = commandFactory)


	@ContextDsl
	fun Route.options(commandFactory: ChottoCommandFactory<Transaction, *, *>) =
		addRoute(method = HttpMethod.Options, commandFactory = commandFactory)


	@ContextDsl
	fun Route.patch(path: String, commandFactory: ChottoCommandFactory<Transaction, *, *>) =
		addRoute(method = HttpMethod.Patch, path = path, commandFactory = commandFactory)


	@ContextDsl
	fun Route.patch(commandFactory: ChottoCommandFactory<Transaction, *, *>) =
		addRoute(method = HttpMethod.Patch, commandFactory = commandFactory)


	@ContextDsl
	fun Route.put(path: String, commandFactory: ChottoCommandFactory<Transaction, *, *>) =
		addRoute(method = HttpMethod.Put, path = path, commandFactory = commandFactory)


	@ContextDsl
	fun Route.put(commandFactory: ChottoCommandFactory<Transaction, *, *>) =
		addRoute(method = HttpMethod.Put, commandFactory = commandFactory)


	inner class Commands internal constructor() {

		internal val handlers: MutableList<ChottoCommandHandler<Transaction, *, *>> = mutableListOf()


		operator fun <Command : ChottoCommand, Result : Any> ChottoCommandFactory<Transaction, Command, Result>.invoke(
			handler: Transaction.() -> (suspend (command: Command) -> Result)
		) {
			handlers += ChottoCommandHandler(
				factory = this,
				handler = handler
			)
		}
	}


	inner class Entities internal constructor() {

		@PublishedApi
		internal val resolvers: MutableMap<KClass<out EntityId>, suspend Transaction.(ids: Set<EntityId>) -> ReceiveChannel<Entity>> = mutableMapOf()


		inline fun <reified Id : EntityId> resolve(noinline resolver: suspend Transaction.(ids: Set<Id>) -> ReceiveChannel<Entity>) {
			@Suppress("UNCHECKED_CAST")
			if (resolvers.putIfAbsent(Id::class, resolver as suspend Transaction.(ids: Set<EntityId>) -> ReceiveChannel<Entity>) != null) {
				error("Cannot register multiple entity resolvers for ${Id::class}")
			}
		}
	}
}
