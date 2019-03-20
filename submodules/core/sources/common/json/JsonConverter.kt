package team.genki.chotto.core

import kotlin.reflect.KClass


expect class JsonConverter<TCommandRequestMeta : CommandRequest.Meta, TCommandResponseMeta : CommandResponse.Meta> internal constructor(
	configuration: JsonConfiguration,
	commandDescriptors: Collection<Command.Typed.Descriptor<*, *>>,
	entityTypes: Collection<EntityType<*, *>>,
	commandRequestMetaClass: KClass<TCommandRequestMeta>,
	commandResponseMetaClass: KClass<TCommandResponseMeta>,
	createDefaultResponseMeta: () -> TCommandResponseMeta?
) {

	fun <TResult : Any> parseCommandResponse(response: String, command: Command.Typed<*, TResult>): CommandResponse<TResult, TCommandResponseMeta>
	fun serializeCommandRequest(request: CommandRequest<*, TCommandRequestMeta>): String
}
