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

	fun <TResult : Any> parseCommandStatus(response: String, command: Command.Typed<*, TResult>): CommandRequest.Status<TResult, TCommandResponseMeta>
	fun serializeCommandRequest(request: CommandRequest<*, TCommandRequestMeta>): String
}


@Suppress("UNCHECKED_CAST")
actual fun <TResult : Any, TCommandResponseMeta : CommandResponse.Meta> JsonConverter<*, TCommandResponseMeta>.parseCommandResponse(
	response: String,
	command: Command.Typed<*, TResult>
) =
	when (val status = parseCommandStatus(response, command)) {
		is CommandRequest.Status.Failure -> throw status.cause
		is CommandRequest.Status.Success -> status.response
	}
