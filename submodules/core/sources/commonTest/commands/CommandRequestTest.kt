package tests

import team.genki.chotto.core.*
import kotlin.test.*


object CommandRequestTest : JsonTest() {

	@Test
	fun test() = testJson(
		value = CommandRequest(
			command = TestCommand(
				property = "value"
			),
			meta = TestCommandRequestMeta(
				property = "value"
			)
		),
		json = """{
			"name":    "test",
			"meta":    {
				"property": "value"
			},
			"command": {
				"property": "value"
			}
		}"""
	)
}
