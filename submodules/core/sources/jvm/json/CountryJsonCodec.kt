package team.genki.chotto.core

import com.github.fluidsonic.fluid.json.*
import com.github.fluidsonic.fluid.stdlib.*


internal object CountryJsonCodec : AbstractJSONCodec<Country, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Country>) =
		readString().let { code ->
			Country.byCode(code) ?: invalidValueError("'$code' is not a valid IANA country code")
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: Country) {
		writeString(value.code)
	}
}
