package team.genki.chotto

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.flatMap
import kotlin.reflect.KClass


internal class EntityResolver<Transaction : ChottoTransaction>(
	private val resolvers: Map<KClass<out EntityId>, suspend Transaction.(ids: Set<EntityId>) -> ReceiveChannel<Entity>>
) {

	suspend fun resolve(ids: Set<EntityId>, transaction: Transaction) =
		ids
			.groupBy { it.factory }
			.map { (factory, ids) ->
				resolvers[factory.idClass]
					?.let { resolve -> transaction.resolve(ids.toSet()) }
					?: GlobalScope.emptyReceiveChannel()
			}
			.toChannel()
			.flatMap { it }
}
