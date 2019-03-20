package tests

import team.genki.chotto.core.*
import kotlin.test.Test


@Suppress("unused")
object AccessTokenTest : JsonTest() {

	@Test
	fun test() = test(
		value = AccessToken("value"),
		json = """ "value" """
	)
}
