package tests

import team.genki.chotto.core.*
import kotlin.test.Test
import kotlin.test.assertFalse


@Suppress("unused")
object AccessTokenTest : JsonTest() {

	@Test
	fun testConfidentiality() {
		assertFalse("does not contain value in toString()") { AccessToken("secretvalue").toString().contains("secretvalue") }
	}


	@Test
	fun testJson() = testJson(
		value = AccessToken("value"),
		json = """ "value" """
	)
}
