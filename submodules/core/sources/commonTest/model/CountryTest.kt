package tests

import com.github.fluidsonic.fluid.stdlib.*
import kotlin.test.*


@Suppress("unused")
object CountryTest : JsonTest() {

	@Test
	fun testJson() = testJson(
		value = Country.byCode("US")!!,
		json = """ "US" """
	)
}
