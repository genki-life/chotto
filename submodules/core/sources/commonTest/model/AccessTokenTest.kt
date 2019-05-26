package tests

import team.genki.chotto.core.*
import kotlin.test.*


object AccessTokenTest {

	@Test
	fun testConfidentiality() {
		assertFalse("does not contain value in toString()") { AccessToken("secretvalue").toString().contains("secretvalue") }
	}


	@Test
	fun testJson() = assertJsonSerialization(
		value = AccessToken("value"),
		json = """ "value" """,
		serializer = AccessToken.serializer()
	)
}
