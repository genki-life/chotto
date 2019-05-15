package team.genki.chotto.core

import com.github.fluidsonic.fluid.json.*
import com.github.fluidsonic.fluid.stdlib.*


object LocalTimeJsonCodec : AbstractJSONCodec<LocalTime, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<LocalTime>) =
		readString().let { raw ->
			LocalTime.parse(raw) ?: invalidValueError("time in ISO-8601 format expected, got '$raw'")
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: LocalTime) =
		writeString(value.toString())
}
