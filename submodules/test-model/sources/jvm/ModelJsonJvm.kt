package tests

import com.github.fluidsonic.fluid.json.*


actual object ModelJson {

	actual inline fun <reified Value : Any> parse(json: String) =
		TestClientModel.jsonConverter.parser.parseValueOfType<Value>(json)


	actual inline fun <reified Value : Any> parseOrNull(json: String) =
		TestClientModel.jsonConverter.parser.parseValueOfTypeOrNull<Value>(json)


	actual fun serialize(value: Any) =
		TestClientModel.jsonConverter.serializer.serializeValue(value)
}
