package team.genki.chotto

import io.ktor.http.Url
import org.bson.BsonReader
import org.bson.BsonWriter


internal object UrlBSONCodec : AbstractBSONCodec<Url, BSONCodingContext>() {

	override fun BsonReader.decode(context: BSONCodingContext) =
		Url(readString())


	override fun BsonWriter.encode(value: Url, context: BSONCodingContext) {
		writeString(value.toString())
	}
}
