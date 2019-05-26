package tests

import team.genki.chotto.core.*
import kotlin.test.*


object PhoneNumberTest {

	@Test
	fun testJson() = assertJsonSerialization(
		value = PhoneNumber("value"),
		json = """ "value" """,
		serializer = PhoneNumber.serializer()
	)
}
