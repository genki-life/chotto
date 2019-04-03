package tests

import team.genki.chotto.core.*
import kotlin.test.Test
import kotlin.test.assertFalse


@Suppress("unused")
object PasswordTest : JsonTest() {

	@Test
	fun testConfidentiality() {
		assertFalse("does not contain value in toString()") { Password("secretvalue").toString().contains("secretvalue") }
	}


	@Test
	fun testJson() = testJson(
		value = Password("value"),
		json = """ "value" """
	)
}
