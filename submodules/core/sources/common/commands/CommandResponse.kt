package team.genki.chotto.core

import team.genki.chotto.core.CommandResponse.*


//@Serializable(with = CommandResponseSerializer::class)
@Suppress("anything")
data class CommandResponse<out TResult : Any, out TMeta : Meta>(
	val entities: Map<EntityId, Entity>,
	val meta: TMeta,
	val result: TResult
) {

	companion object


	interface Meta
}

//
//@Serializer(forClass = CommandResponse::class)
//internal class CommandResponseSerializer<TResult : Any, TMeta : Meta> : KSerializer<CommandResponse<TResult, TMeta>> {
//
//	override fun deserialize(decoder: Decoder): CommandResponse<TResult, TMeta> =
//		TODO()
//
//
//	override fun serialize(encoder: Encoder, obj: CommandResponse<TResult, TMeta>) {
//
//	}
//}
