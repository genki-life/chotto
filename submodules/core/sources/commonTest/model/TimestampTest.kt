package tests

import team.genki.chotto.core.*
import kotlin.test.Test


@Suppress("unused")
object TimestampTest : JsonTest() {

	@Test
	fun testJson() = testJson(
		value = Timestamp(unixTimestamp = 0),
		json = """ "1970-01-01T00:00:00Z" """
	)
}
