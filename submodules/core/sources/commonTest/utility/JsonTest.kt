package tests

import kotlin.test.*


open class JsonTest {

	internal inline fun <reified Value : Any> testJson(
		value: Value,
		json: String,
		noinline equals: ((a: Value, b: Value) -> Boolean)? = null
	) {
		val actualJson = ModelJson.serialize(value)
		val actualStructure = try {
			ModelJson.parseOrNull<Any>(actualJson)
		}
		catch (e: Exception) {
			throw Exception("Cannot parse actual JSON:\n$actualJson", e)
		}

		val expectedStructure = try {
			ModelJson.parseOrNull<Any>(json)
		}
		catch (e: Exception) {
			throw Exception("Cannot parse expected JSON:\n$json", e)
		}

		assertEquals(expectedStructure, actualStructure, "serialized value")

		val actualValue = ModelJson.parse<Value>(json)

		if (equals != null)
			assertTrue(equals(value, actualValue), "parsed value ==> expected: $value but was: $actualValue")
		else
			assertEquals(value, actualValue, "parsed value")
	}
}
