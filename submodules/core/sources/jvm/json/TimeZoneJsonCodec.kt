package team.genki.chotto.core

import com.github.fluidsonic.fluid.json.*
import com.github.fluidsonic.fluid.stdlib.*


object TimeZoneJsonCodec : AbstractJSONCodec<TimeZone, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<TimeZone>) =
		readString().let { id ->
			TimeZone.withId(id) ?: invalidValueError("IANA time zone name expected but got \"$id\"")
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: TimeZone) =
		writeString(value.id)
}
