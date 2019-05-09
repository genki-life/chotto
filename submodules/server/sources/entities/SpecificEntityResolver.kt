package team.genki.chotto.server

import kotlinx.coroutines.channels.*
import team.genki.chotto.core.*


internal class SpecificEntityResolver<Id : EntityId.Typed<Id, *>, Entity : ServerEntity.Typed<Id, *, Transaction>, in Transaction : ChottoTransaction>(
	val type: EntityType<out Id, *>,
	val resolver: suspend (ids: Iterable<Id>) -> ReceiveChannel<Entity>
)
