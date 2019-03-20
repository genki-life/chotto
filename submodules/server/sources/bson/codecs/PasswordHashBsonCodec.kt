package team.genki.chotto.server

import org.bson.BsonReader
import org.bson.BsonWriter


internal object PasswordHashBsonCodec : AbstractBsonCodec<PasswordHash, BsonCodingContext>() {

	override fun BsonReader.decode(context: BsonCodingContext) =
		PasswordHash(readString())


	override fun BsonWriter.encode(value: PasswordHash, context: BsonCodingContext) {
		writeString(value.value)
	}
}
