package team.genki.chotto.client.model

import com.github.fluidsonic.fluid.json.*
import kotlin.reflect.KClass


internal class CommandResponseJsonCodec(
	private val metaClass: KClass<out CommandResponse.Meta>,
	private val createDefaultMeta: () -> CommandResponse.Meta?
) : AbstractJSONCodec<CommandResponse<*, *>, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<CommandResponse<*, *>>): CommandResponse<*, *> {
		var entities: Map<EntityId, Entity>? = null
		var meta: CommandResponse.Meta? = null
		var result: Any? = null

		readFromMapByElementValue { key ->
			when (key) {
				"entities" -> entities = readValueOfType()
				"meta" -> meta = readValueOfTypeOrNull(jsonCodingType(metaClass))
				"result" -> result = readValueOfType(valueType.arguments[0])
				else -> skipValue()
			}
		}

		return CommandResponse(
			entities = entities ?: emptyMap(),
			meta = meta ?: createDefaultMeta() ?: missingPropertyError("meta"),
			result = result ?: missingPropertyError("result")
		)
	}


	override fun JSONEncoder<JSONCodingContext>.encode(value: CommandResponse<*, *>) {
		writeIntoMap {
			writeMapElement("entities", value = value.entities)
			writeMapElement("meta", value = value.meta)
			writeMapElement("result", value = value.result)
		}
	}
}
