package tests

import kotlinx.serialization.*
import kotlinx.serialization.internal.*
import team.genki.chotto.core.*
import kotlin.test.*


object ChangeTest {

	@Test
	fun testJson() = assertJsonSerialization(
		value = Change("value"),
		json = """ "value" """,
		serializer = Change.serializer(String.serializer())
	)


	@Test
	fun testJsonNull() = assertJsonSerialization(
		value = Change(null),
		json = """ null """,
		serializer = Change.serializer(makeNullable(String.serializer()))
	)
}
