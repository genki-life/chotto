package team.genki.chotto.core

import com.github.fluidsonic.fluid.json.*


internal object CurrencyJsonCodec : AbstractJSONCodec<Currency, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Currency>) =
		readString().let { code ->
			Currency.byCode(code) ?: invalidValueError("'$code' is not a valid ISO 4217 currency code")
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: Currency) {
		writeString(value.code)
	}
}
