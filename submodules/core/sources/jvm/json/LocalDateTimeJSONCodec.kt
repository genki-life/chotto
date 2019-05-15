package team.genki.chotto.core

import com.github.fluidsonic.fluid.json.*
import com.github.fluidsonic.fluid.stdlib.*


object LocalDateTimeJsonCodec : AbstractJSONCodec<LocalDateTime, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<LocalDateTime>) =
		readString().let { raw ->
			LocalDateTime.parse(raw) ?: invalidValueError("date and time in ISO-8601 format expected, got '$raw'")
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: LocalDateTime) =
		writeString(value.toString())
}
