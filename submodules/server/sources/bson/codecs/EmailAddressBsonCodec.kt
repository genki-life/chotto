package team.genki.chotto.server

import team.genki.chotto.core.*


internal object EmailAddressBsonCodec : AbstractBsonCodec<EmailAddress, BsonCodingContext>() {

	override fun BsonDecoder.decode(context: BsonCodingContext) =
		EmailAddress(readString())


	override fun BsonEncoder.encode(value: EmailAddress, context: BsonCodingContext) {
		writeString(value.value)
	}
}
