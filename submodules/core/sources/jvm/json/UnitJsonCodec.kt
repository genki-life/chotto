package team.genki.chotto.core

import com.github.fluidsonic.fluid.json.*


internal object UnitJsonCodec : AbstractJSONCodec<Unit, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Unit>) {
		readFromMap {
			while (nextToken != JSONToken.mapEnd)
				skipValue()
		}
	}


	override fun JSONEncoder<JSONCodingContext>.encode(value: Unit) {
		writeIntoMap {}
	}
}
