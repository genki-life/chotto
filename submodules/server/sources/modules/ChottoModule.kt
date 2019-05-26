package team.genki.chotto.server

import kotlinx.coroutines.channels.*
import team.genki.chotto.core.*


abstract class ChottoModule<Context : ChottoServerContext, Transaction : ChottoTransaction> {

	internal fun configurationForContext(context: Context) =
		ContextConfiguration(context).apply { configure() }


	internal fun configurationForTransaction(transaction: Transaction) =
		TransactionConfiguration(transaction).apply { configure() }


	open fun ContextConfiguration.configure() {}
	open fun TransactionConfiguration.configure() {}


	inner class ContextConfiguration internal constructor(
		val context: Context
	) {

		val bson = Bson()
		val ids = Ids()


		inner class Bson internal constructor() {

			internal val codecProviders = mutableListOf<BsonCodecProvider<Context>>()


			operator fun invoke(vararg codecProviders: BsonCodecProvider<Context>) {
				this.codecProviders += codecProviders
			}
		}


		inner class Ids internal constructor() {

			internal val types = mutableListOf<EntityType.Typed<*, *>>()


			operator fun invoke(vararg types: EntityType.Typed<*, *>) {
				this.types += types
			}
		}
	}


	inner class TransactionConfiguration internal constructor(
		val transaction: Transaction
	) {

		val commands = Commands(transaction)
		val entities = Entities(transaction)


		// move once fixed https://youtrack.jetbrains.com/issue/KT-10468
		infix fun <TCommand : Command.Typed<TCommand, TResult>, TResult : Any> TCommand.handleBy(
			handler: suspend (command: TCommand) -> TResult
		) =
			descriptor handleBy handler


		// move once fixed https://youtrack.jetbrains.com/issue/KT-10468
		infix fun <TCommand : Command.Typed<TCommand, TResult>, TResult : Any> Command.Typed.Descriptor<out TCommand, out TResult>.handleBy(
			handler: suspend (command: TCommand) -> TResult
		) {
			commands.handlers += SpecificCommandHandler(
				descriptor = this,
				handler = handler
			)
		}


		// move once fixed https://youtrack.jetbrains.com/issue/KT-10468
		infix fun <Id : EntityId.Typed<Id, *>, Entity : ServerEntity.Typed<Id, *, Transaction>> EntityType<out Id, *>.resolveBy(
			resolver: suspend (ids: Iterable<Id>) -> ReceiveChannel<Entity>
		) {
			entities.resolvers += SpecificEntityResolver(
				type = this,
				resolver = resolver
			)
		}


		inner class Commands internal constructor(
			val transaction: Transaction // don't use from outer class until fixed: https://youtrack.jetbrains.com/issue/KT-27124
		) {

			internal val handlers = mutableListOf<SpecificCommandHandler<*, *>>()


			inline operator fun invoke(configure: Transaction.() -> Unit) {
				transaction.configure()
			}
		}


		inner class Entities internal constructor(
			val transaction: Transaction // don't use from outer class until fixed: https://youtrack.jetbrains.com/issue/KT-27124
		) {

			internal val resolvers = mutableListOf<SpecificEntityResolver<*, *, Transaction>>()


			inline operator fun invoke(configure: Transaction.() -> Unit) {
				transaction.configure()
			}
		}
	}
}
