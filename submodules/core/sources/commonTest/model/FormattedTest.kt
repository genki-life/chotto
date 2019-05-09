package tests

import team.genki.chotto.core.*
import kotlin.test.*


@Suppress("unused")
object FormattedTest : JsonTest() {

	@Test
	fun testJson() = testJson(
		value = Formatted("raw", "formatted"),
		json = """{
			"formatted": "formatted",
			"raw":       "raw"
		}"""
	)
}
