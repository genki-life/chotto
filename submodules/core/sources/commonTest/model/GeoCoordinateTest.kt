package tests

import team.genki.chotto.core.*
import kotlin.test.Test


@Suppress("unused")
object GeoCoordinateTest : JsonTest() {

	@Test
	fun testJson() = testJson(
		value = GeoCoordinate(1.2, 3.4),
		json = """{
			"latitude":  1.2,
			"longitude": 3.4
		}"""
	)
}
