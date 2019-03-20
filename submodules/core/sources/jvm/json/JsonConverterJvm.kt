package team.genki.chotto.core

import com.github.fluidsonic.fluid.json.*
import kotlin.reflect.KClass


actual class JsonConverter<TCommandRequestMeta : CommandRequest.Meta, TCommandResponseMeta : CommandResponse.Meta> actual constructor(
	configuration: JsonConfiguration,
	commandDescriptors: Collection<Command.Typed.Descriptor<*, *>>,
	entityTypes: Collection<EntityType<*, *>>,
	val commandRequestMetaClass: KClass<TCommandRequestMeta>,
	val commandResponseMetaClass: KClass<TCommandResponseMeta>,
	createDefaultResponseMeta: () -> TCommandResponseMeta?
) {

	val modelCodecProvider = JSONCodecProvider(
		configuration.modelCodecProvider,
		JSONCodecProvider.generated(GeneratedJsonCodecProvider::class)
	)
	val codecProvider = JSONCodecProvider(
		modelCodecProvider,
		CommandRequestJsonCodec(metaClass = commandRequestMetaClass, descriptors = commandDescriptors),
		CommandResponseJsonCodec(metaClass = commandResponseMetaClass, createDefaultMeta = createDefaultResponseMeta),
		EntityIdJsonCodec(entityTypeByNamespace = entityTypes.associateBy { it.namespace }),
		JSONCodecProvider(entityTypes.map { SpecificEntityIdJsonCodec(it as EntityType<Nothing, *>) }),
		EntityMapJsonCodec,
		EnumJSONCodecProvider(transformation = EnumJSONTransformation.ToString(EnumJSONTransformation.Case.lowercase_words))
	)
	val parser = JSONCodingParser.builder().decodingWith(codecProvider).build()
	val serializer = JSONCodingSerializer.builder().encodingWith(codecProvider).build()


	@Suppress("UNCHECKED_CAST")
	actual fun <TResult : Any> parseCommandResponse(response: String, command: Command.Typed<*, TResult>) =
		parser.parseValueOfType(
			source = response,
			valueType = jsonCodingType(CommandResponse::class, command.descriptor.resultClass, commandResponseMetaClass)
		) as CommandResponse<TResult, TCommandResponseMeta>


	actual fun serializeCommandRequest(request: CommandRequest<*, TCommandRequestMeta>) =
		serializer.serializeValue(request)
}
