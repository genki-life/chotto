package team.genki.chotto.client.model

import com.github.fluidsonic.fluid.json.*


internal class SpecificEntityIdJsonCodec<Id : EntityId>(
	private val type: EntityType<Id, *>
) : JSONCodec<Id, JSONCodingContext> {

	override val decodableType = jsonCodingType(type.idClass)


	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Id>) =
		readString().let { string ->
			type.parseId(string) ?: invalidValueError("'$string' is not a valid '${type.namespace}' ID")
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: Id) {
		writeString(type.serializeId(value))
	}
}
