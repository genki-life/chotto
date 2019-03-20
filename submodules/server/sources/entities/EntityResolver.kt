package team.genki.chotto.server

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.flatMap
import team.genki.chotto.core.*
import java.util.IdentityHashMap


// FIXME
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
			// FIXME convert to client model?
			.flatMap { it }
}
