package team.genki.chotto.server

import com.github.fluidsonic.fluid.time.*


internal object TimeZoneBsonCodec : AbstractBsonCodec<TimeZone, BsonCodingContext>(includesSubclasses = true) {

	override fun BsonDecoder.decode(context: BsonCodingContext) =
		readString().let { id ->
			TimeZone.withId(id) ?: error("invalid timezone: $id")
		}


	override fun BsonEncoder.encode(value: TimeZone, context: BsonCodingContext) {
		writeString(value.id)
	}
}
