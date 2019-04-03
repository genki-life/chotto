package tests

import team.genki.chotto.core.*
import kotlin.test.Test
import kotlin.test.assertFalse


@Suppress("unused")
object RefreshTokenTest : JsonTest() {

	@Test
	fun testConfidentiality() {
		assertFalse("does not contain value in toString()") { RefreshToken("secretvalue").toString().contains("secretvalue") }
	}


	@Test
	fun testJson() = testJson(
		value = RefreshToken("value"),
		json = """ "value" """
	)
}
