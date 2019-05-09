package team.genki.chotto.core

import com.github.fluidsonic.fluid.json.*
import com.github.fluidsonic.fluid.stdlib.*


internal object TimestampJsonCodec : AbstractJSONCodec<Timestamp, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Timestamp>) =
		readString().let { raw ->
			Timestamp.parse(raw) ?: invalidValueError("date, time and time zone in ISO-8601 format expected, got '$raw'")
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: Timestamp) =
		writeString(value.toString())
}
