package tests

import kotlin.test.assertEquals


open class JsonTest {

	internal inline fun <reified Value : Any> testJson(
		value: Value,
		json: String
	) {
		val actualJson = ModelJson.serialize(value)
		val actualStructure = try {
			ModelJson.parse<Any>(actualJson)
		}
		catch (e: Exception) {
			throw AssertionError("Cannot parse actual JSON:\n$actualJson\n\n$e")
		}

		val expectedStructure = try {
			ModelJson.parse<Any>(json)
		}
		catch (e: Exception) {
			throw AssertionError("Cannot parse expected JSON:\n$json\n\n$e")
		}

		assertEquals(expectedStructure, actualStructure, "serialized value")

		val actualValue = ModelJson.parse<Value>(json)

		assertEquals(value, actualValue, "parsed value")
	}
}
