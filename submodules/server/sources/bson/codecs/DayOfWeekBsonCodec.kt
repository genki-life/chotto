package team.genki.chotto.server

import com.github.fluidsonic.fluid.time.*


internal object DayOfWeekBsonCodec : AbstractBsonCodec<DayOfWeek, BsonCodingContext>() {

	override fun BsonDecoder.decode(context: BsonCodingContext) =
		readString().let { name ->
			DayOfWeek.values().firstOrNull { it.name == name } ?: error("invalid day of week: $name")
		}


	override fun BsonEncoder.encode(value: DayOfWeek, context: BsonCodingContext) {
		writeString(value.name)
	}
}
