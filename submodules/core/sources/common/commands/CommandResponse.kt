package team.genki.chotto.core

import kotlinx.serialization.*


@Serializable
data class CommandResponse<out TResult : Any, out TMeta : CommandResponseMeta>(
	@Serializable(with = EntityMapSerializer::class)
	val entities: Map<EntityId, Entity> = emptyMap(),
	val meta: TMeta,
	val result: TResult
) {

	companion object
}


interface CommandResponseMeta
