package team.genki.chotto.server

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import team.genki.chotto.core.*
import java.util.*


// FIXME rework this
internal class EntityResolver<in Transaction : ChottoTransaction>(
	resolvers: Collection<SpecificEntityResolver<*, *, Transaction>>
) {

	private val resolverByType =
		resolvers.associateTo(IdentityHashMap()) { it.type to it.resolver }


	suspend fun resolve(ids: Set<EntityId>) =
		ids
			.groupBy { it.type }
			.map { (type, ids) ->
				resolverByType[type]
					?.let { it as suspend (ids: Iterable<EntityId>) -> ReceiveChannel<ServerEntity.Typed<*, *, Transaction>> }
					?.let { it(ids) }
					?: GlobalScope.emptyReceiveChannel()
			}
			.toChannel()
			// FIXME automatically convert to client model here?
			.flatMap { it }
}
