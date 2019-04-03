package tests

import team.genki.chotto.core.*
import kotlin.test.Test


@Suppress("unused")
object ChangeTest : JsonTest() {

	@Test
	fun testJson() = testJson(
		value = Change("value"),
		json = """ "value" """
	)


	@Test
	fun testJsonNull() = testJson(
		value = Change<String?>(null),
		json = """ null """
	)
}
