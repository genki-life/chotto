package team.genki.chotto.server


internal object PasswordHashBsonCodec : AbstractBsonCodec<PasswordHash, BsonCodingContext>() {

	override fun BsonDecoder.decode(context: BsonCodingContext) =
		PasswordHash(readString())


	override fun BsonEncoder.encode(value: PasswordHash, context: BsonCodingContext) {
		writeString(value.value)
	}
}
