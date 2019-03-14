package team.genki.chotto.client.model

import kotlin.reflect.KClass


expect class JsonConverter<CommandRequestMeta : CommandRequest.Meta, CommandResponseMeta : CommandResponse.Meta>(
	configuration: JsonConfiguration,
	commandDescriptors: Collection<Command.Descriptor<*, *>>,
	entityTypes: Collection<EntityType<*, *>>,
	commandRequestMetaClass: KClass<CommandRequestMeta>,
	commandResponseMetaClass: KClass<CommandResponseMeta>,
	createDefaultResponseMeta: () -> CommandResponseMeta?
) {

	fun <Result : Any> parseCommandResponse(response: String, command: Command<Result>): CommandResponse<Result, CommandResponseMeta>
	fun serializeCommandRequest(request: CommandRequest<*, CommandRequestMeta>): String
}
