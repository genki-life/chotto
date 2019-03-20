package team.genki.chotto.core

import com.github.fluidsonic.fluid.json.*


internal object EntityMapJsonCodec : AbstractJSONDecoderCodec<Map<out EntityId, Entity>, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Map<out EntityId, Entity>>) =
		readMapByElement {
			val id = readValueOfType<EntityId>()
			val entity = readValueOfType(jsonCodingType(id.type.entityClass))
			if (entity.id != id)
				invalidValueError("entity has unexpected ID '${entity.id}'")

			entity.id to entity
		}
}
