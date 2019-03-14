package team.genki.chotto.client.model

import com.github.fluidsonic.fluid.json.*


internal class EntityIdJsonCodec(
	private val entityTypeByNamespace: Map<String, EntityType<*, *>>
) : AbstractJSONDecoderCodec<EntityId, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<EntityId>) =
		readString().let { value ->
			val typeName = value.substringBeforeLast('/')
			entityTypeByNamespace[typeName]?.parseId(value) ?: invalidValueError("'$value' is not a valid ID")
		}
}
