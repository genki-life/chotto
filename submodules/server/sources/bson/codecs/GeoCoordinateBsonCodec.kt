package team.genki.chotto.server

import org.bson.BsonReader
import org.bson.BsonWriter
import team.genki.chotto.core.*


internal object GeoCoordinateBsonCodec : AbstractBsonCodec<GeoCoordinate, BsonCodingContext>() {

	override fun BsonReader.decode(context: BsonCodingContext): GeoCoordinate {
		var coordinate: GeoCoordinate? = null
		var type: String? = null

		readDocumentWithValues { fieldName ->
			when (fieldName) {
				"coordinates" -> coordinate = readArray {
					val longitude = readDouble()
					val latitude = readDouble()

					GeoCoordinate(latitude = latitude, longitude = longitude)
				}
				"type" -> type = readString()
				else -> skipValue()
			}
		}

		if (type != "Point") error("invalid type for GeoCoordinate: $type")
		return coordinate ?: error("missing coordinate")
	}


	override fun BsonWriter.encode(value: GeoCoordinate, context: BsonCodingContext) {
		writeStartDocument()
		writeName("coordinates")
		writeStartArray()
		writeDouble(value.longitude)
		writeDouble(value.latitude)
		writeEndArray()
		writeName("type")
		writeString("Point")
		writeEndDocument()
	}
}
