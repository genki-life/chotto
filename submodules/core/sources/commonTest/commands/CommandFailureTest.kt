package tests

import team.genki.chotto.core.*
import kotlin.test.*


object CommandFailureTest {

	@Test
	fun test() = assertJsonSerialization(
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
		serializer = CommandFailure.serializer(),
		equals = { a, b ->
			a.code == b.code && a.developerMessage == b.developerMessage && a.userMessage == b.userMessage && a.cause == b.cause
		}
	)
}
