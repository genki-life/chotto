package tests

import team.genki.chotto.core.*
import kotlin.test.*


object PasswordTest {

	@Test
	fun testConfidentiality() {
		assertFalse("does not contain value in toString()") { Password("secretvalue").toString().contains("secretvalue") }
	}


	@Test
	fun testJson() = assertJsonSerialization(
		value = Password("value"),
		json = """ "value" """,
		serializer = Password.serializer()
	)
}
