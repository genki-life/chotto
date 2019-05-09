package tests

import com.github.fluidsonic.fluid.stdlib.*
import kotlin.test.*


@Suppress("unused")
object TimestampTest : JsonTest() {

	@Test
	fun testJson() = testJson(
		value = Timestamp(epochSecond = 0),
		json = """ "1970-01-01T00:00:00Z" """
	)
}
