package tests

import com.github.fluidsonic.fluid.stdlib.*
import kotlin.test.*


@Suppress("unused")
object CurrencyTest : JsonTest() {

	@Test
	fun testJson() = testJson(
		value = Currency.byCode("EUR")!!,
		json = """ "EUR" """
	)
}
