package tests

import team.genki.chotto.core.*
import kotlin.test.Test


@Suppress("unused")
object CurrencyTest : JsonTest() {

	@Test
	fun testJson() = testJson(
		value = Currency.byCode("EUR")!!,
		json = """ "EUR" """
	)
}
