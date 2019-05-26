package team.genki.chotto.server

import com.github.fluidsonic.fluid.time.*
import org.bson.*


internal object TimeZoneBsonCodec : AbstractBsonCodec<TimeZone, BsonCodingContext>(includesSubclasses = true) {

	override fun BsonReader.decode(context: BsonCodingContext) =
		readString().let { id ->
			TimeZone.withId(id) ?: error("invalid timezone: $id")
		}


	override fun BsonWriter.encode(value: TimeZone, context: BsonCodingContext) {
		writeString(value.id)
	}
}
