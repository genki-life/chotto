package team.genki.chotto.server

import org.bson.BsonReader
import org.bson.BsonWriter
import team.genki.chotto.core.*


internal object TimezoneBsonCodec : AbstractBsonCodec<Timezone, BsonCodingContext>(includesSubclasses = true) {

	override fun BsonReader.decode(context: BsonCodingContext) =
		readString().let { id ->
			Timezone.byId(id) ?: error("invalid timezone: $id")
		}


	override fun BsonWriter.encode(value: Timezone, context: BsonCodingContext) {
		writeString(value.id)
	}
}
