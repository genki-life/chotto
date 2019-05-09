package team.genki.chotto.core

import com.github.fluidsonic.fluid.json.*
import kotlin.reflect.*


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

	@Suppress("UNCHECKED_CAST")
	val codecProvider = JSONCodecProvider(
		modelCodecProvider,
		CountryJsonCodec,
		CurrencyJsonCodec,
		CommandFailureJsonCodec,
		CommandRequestJsonCodec(metaClass = commandRequestMetaClass, descriptors = commandDescriptors),
		CommandResponseJsonCodec(metaClass = commandResponseMetaClass, createDefaultMeta = createDefaultResponseMeta),
		TimestampJsonCodec,
		TimeZoneJsonCodec,
		UnitJsonCodec,
		EntityIdJsonCodec(entityTypeByNamespace = entityTypes.associateBy { it.namespace }),
		JSONCodecProvider(entityTypes.map { SpecificEntityIdJsonCodec(it as EntityType<Nothing, *>) }),
		EntityMapJsonCodec,
		EnumJSONCodecProvider(transformation = EnumJSONTransformation.ToString(EnumJSONTransformation.Case.lowercase_words))
	)

	val parser = JSONCodingParser.builder().decodingWith(codecProvider).build()
	val serializer = JSONCodingSerializer.builder().encodingWith(codecProvider).build()


	@Suppress("UNCHECKED_CAST")
	actual fun <TResult : Any> parseCommandStatus(response: String, command: Command.Typed<*, TResult>) =
		try {
			parser.parseValueOfType(
				source = response,
				valueType = jsonCodingType(CommandRequest.Status::class, command.descriptor.resultClass, commandResponseMetaClass)
			) as CommandRequest.Status<TResult, TCommandResponseMeta>
		}
		catch (e: JSONException) {
			CommandRequest.Status.Failure<TResult, TCommandResponseMeta>(CommandFailure.invalidResponse(response, cause = e))
		}


	actual fun serializeCommandRequest(request: CommandRequest<*, TCommandRequestMeta>) =
		serializer.serializeValue(request)
}
