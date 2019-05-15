package team.genki.chotto.core

import com.github.fluidsonic.fluid.json.*
import com.github.fluidsonic.fluid.stdlib.*


object LocalDateJsonCodec : AbstractJSONCodec<LocalDate, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<LocalDate>) =
		readString().let { raw ->
			LocalDate.parse(raw) ?: invalidValueError("date in ISO-8601 format expected, got '$raw'")
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: LocalDate) =
		writeString(value.toString())
}
