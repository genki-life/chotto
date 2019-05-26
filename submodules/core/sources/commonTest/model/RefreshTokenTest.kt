package tests

import team.genki.chotto.core.*
import kotlin.test.*


object RefreshTokenTest {

	@Test
	fun testConfidentiality() {
		assertFalse("does not contain value in toString()") { RefreshToken("secretvalue").toString().contains("secretvalue") }
	}


	@Test
	fun testJson() = assertJsonSerialization(
		value = RefreshToken("value"),
		json = """ "value" """,
		serializer = RefreshToken.serializer()
	)
}
