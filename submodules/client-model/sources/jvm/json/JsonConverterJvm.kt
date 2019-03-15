package team.genki.chotto.client.model

import com.github.fluidsonic.fluid.json.*
import kotlin.reflect.KClass


actual class JsonConverter<CommandRequestMeta : CommandRequest.Meta, CommandResponseMeta : CommandResponse.Meta> actual constructor(
	configuration: JsonConfiguration,
	commandDescriptors: Collection<Command.Descriptor<*, *>>,
	entityTypes: Collection<EntityType<*, *>>,
	commandRequestMetaClass: KClass<CommandRequestMeta>,
	private val commandResponseMetaClass: KClass<CommandResponseMeta>,
	createDefaultResponseMeta: () -> CommandResponseMeta?
) {

	val codecProvider = JSONCodecProvider(
		configuration.codecProvider,
		JSONCodecProvider.generated(GeneratedJsonCodecProvider::class),
		CommandRequestJsonCodec(metaClass = commandRequestMetaClass, commandDescriptorByName = commandDescriptors.associateBy { it.name }),
		CommandResponseJsonCodec(metaClass = commandResponseMetaClass, createDefaultMeta = createDefaultResponseMeta),
		EntityIdJsonCodec(entityTypeByNamespace = entityTypes.associateBy { it.namespace }),
		EntityMapJsonCodec,
		JSONCodecProvider(entityTypes.map { SpecificEntityIdJsonCodec(it) }),
		EnumJSONCodecProvider(transformation = EnumJSONTransformation.ToString(EnumJSONTransformation.Case.lowercase_words))
	)

	val parser = JSONCodingParser.builder().decodingWith(codecProvider).build()
	val serializer = JSONCodingSerializer.builder().encodingWith(codecProvider).build()


	@Suppress("UNCHECKED_CAST")
	actual fun <Result : Any> parseCommandResponse(response: String, command: Command<Result>) =
		parser.parseValueOfType(
			source = response,
			valueType = jsonCodingType(CommandResponse::class, command.descriptor.resultClass, commandResponseMetaClass)
		) as CommandResponse<Result, CommandResponseMeta>


	actual fun serializeCommandRequest(request: CommandRequest<*, CommandRequestMeta>) =
		serializer.serializeValue(request)
}
