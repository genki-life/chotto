package team.genki.chotto.server

import io.fluidsonic.stdlib.*


internal object CountryBsonCodec : AbstractBsonCodec<Country, BsonCodingContext>() {

	override fun BsonDecoder.decode(context: BsonCodingContext) =
		readString().let { code ->
			Country.byCode(code) ?: error("invalid country code: $code")
		}


	override fun BsonEncoder.encode(value: Country, context: BsonCodingContext) {
		writeString(value.code)
	}
}
