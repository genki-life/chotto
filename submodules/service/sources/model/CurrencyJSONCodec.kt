package team.genki.chotto

import com.github.fluidsonic.fluid.json.*
import java.util.Currency


internal object CurrencyJSONCodec : AbstractJSONCodec<Currency, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Currency>) =
		readString().let { code ->
			runCatching { Currency.getInstance(code) }.getOrNull() ?: invalidValueError("'$code' is not a valid ISO 4217 currency code")
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: Currency) {
		writeString(value.currencyCode)
	}
}
