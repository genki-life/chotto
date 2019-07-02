package team.genki.chotto.server

import team.genki.chotto.core.*


internal object PhoneNumberBsonCodec : AbstractBsonCodec<PhoneNumber, BsonCodingContext>() {

	override fun BsonDecoder.decode(context: BsonCodingContext) =
		PhoneNumber(readString())


	override fun BsonEncoder.encode(value: PhoneNumber, context: BsonCodingContext) {
		writeString(value.value)
	}
}
