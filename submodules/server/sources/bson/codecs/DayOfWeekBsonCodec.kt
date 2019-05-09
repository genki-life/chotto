package team.genki.chotto.server

import com.github.fluidsonic.fluid.stdlib.*
import org.bson.*


internal object DayOfWeekBsonCodec : AbstractBsonCodec<DayOfWeek, BsonCodingContext>() {

	override fun BsonReader.decode(context: BsonCodingContext) =
		readString().let { name ->
			DayOfWeek.values().firstOrNull { it.name == name } ?: error("invalid day of week: $name")
		}


	override fun BsonWriter.encode(value: DayOfWeek, context: BsonCodingContext) {
		writeString(value.name)
	}
}
