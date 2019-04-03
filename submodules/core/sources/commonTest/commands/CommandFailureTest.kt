package tests

import team.genki.chotto.core.*
import kotlin.test.Test


object CommandFailureTest : JsonTest() {

	@Test
	fun test() = testJson(
		value = CommandFailure(
			code = "some code",
			developerMessage = "some developer message",
			userMessage = "some user message"
		),
		json = """{
			"code": "some code",
			"developerMessage": "some developer message",
			"userMessage": "some user message"
		}""",
		equals = { a, b ->
			a.code == b.code && a.developerMessage == b.developerMessage && a.userMessage == b.userMessage && a.cause == b.cause
		}
	)
}
