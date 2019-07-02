package team.genki.chotto.server

import com.github.fluidsonic.fluid.stdlib.*


internal object CurrencyBsonCodec : AbstractBsonCodec<Currency, BsonCodingContext>() {

	override fun BsonDecoder.decode(context: BsonCodingContext) =
		readString().let { code ->
			Currency.byCode(code) ?: error("invalid currency code: $code")
		}


	override fun BsonEncoder.encode(value: Currency, context: BsonCodingContext) {
		writeString(value.code)
	}
}
