package tests

import team.genki.chotto.core.*
import kotlin.test.*


object CommandRequestTest {

	@Test
	fun test() = assertJsonSerialization(
		value = CommandRequest(
			command = TestCommand(
				property = "value"
			),
			meta = TestCommandRequestMeta(
				property = "value"
			)
		),
		json = """{
			"meta":    {
				"property": "value"
			},
			"commandName": "test",
			"command": {
				"property": "value"
			}
		}""",
		serializer = CommandRequest.serializer()
	)
}
