package tests

import team.genki.chotto.core.*
import kotlin.test.*


object EmailAddressTest {

	@Test
	fun testJson() = assertJsonSerialization(
		value = EmailAddress("value"),
		json = """ "value" """,
		serializer = EmailAddress.serializer()
	)
}
