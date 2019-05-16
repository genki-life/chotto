package team.genki.chotto.server

import com.github.fluidsonic.fluid.stdlib.*
import org.bson.*


internal object CurrencyBsonCodec : AbstractBsonCodec<Currency, BsonCodingContext>() {

	override fun BsonReader.decode(context: BsonCodingContext) =
		readString().let { code ->
			Currency.byCode(code) ?: error("invalid currency code: $code")
		}


	override fun BsonWriter.encode(value: Currency, context: BsonCodingContext) {
		writeString(value.code)
	}
}
