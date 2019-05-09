package team.genki.chotto.server

import org.bson.*
import team.genki.chotto.core.*


internal object EmailAddressBsonCodec : AbstractBsonCodec<EmailAddress, BsonCodingContext>() {

	override fun BsonReader.decode(context: BsonCodingContext) =
		EmailAddress(readString())


	override fun BsonWriter.encode(value: EmailAddress, context: BsonCodingContext) {
		writeString(value.value)
	}
}
