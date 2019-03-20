package team.genki.chotto.server

import org.bson.BsonReader
import org.bson.BsonWriter
import team.genki.chotto.core.*


internal object CountryBsonCodec : AbstractBsonCodec<Country, BsonCodingContext>() {

	override fun BsonReader.decode(context: BsonCodingContext) =
		readString().let { code ->
			Country.byCode(code) ?: error("invalid country code: $code")
		}


	override fun BsonWriter.encode(value: Country, context: BsonCodingContext) {
		writeString(value.code)
	}
}
