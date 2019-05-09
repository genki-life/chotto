package tests

import team.genki.chotto.core.*
import kotlin.test.*


@Suppress("unused")
object EmailAddressTest : JsonTest() {

	@Test
	fun testJson() = testJson(
		value = EmailAddress("value"),
		json = """ "value" """
	)
}
