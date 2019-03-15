package team.genki.chotto

import org.bson.BsonReader
import org.bson.BsonWriter


internal object FirstNameBSONCodec : AbstractBSONCodec<FirstName, BSONCodingContext>() {

	override fun BsonReader.decode(context: BSONCodingContext) =
		FirstName(readString())


	override fun BsonWriter.encode(value: FirstName, context: BSONCodingContext) {
		writeString(value.value)
	}
}
