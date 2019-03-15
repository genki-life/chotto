package team.genki.chotto

import com.github.fluidsonic.fluid.json.*
import io.ktor.http.Url


internal object UrlJSONCodec : AbstractJSONCodec<Url, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Url>) =
		Url(readString())


	override fun JSONEncoder<JSONCodingContext>.encode(value: Url) {
		writeString(value.toString())
	}
}
