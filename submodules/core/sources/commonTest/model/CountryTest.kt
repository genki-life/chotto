package tests

import team.genki.chotto.core.*
import kotlin.test.Test


@Suppress("unused")
object CountryTest : JsonTest() {

	@Test
	fun testJson() = testJson(
		value = Country.byCode("US")!!,
		json = """ "US" """
	)
}
