package tests

import team.genki.chotto.core.*
import kotlin.test.Test


@Suppress("unused")
object FormattedTimestampTest : JsonTest() {

	@Test
	fun testJson() = testJson(
		value = FormattedTimestamp("value"),
		json = """ "value" """
	)
}
