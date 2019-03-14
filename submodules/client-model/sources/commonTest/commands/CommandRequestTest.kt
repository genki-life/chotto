package tests

import team.genki.chotto.client.model.*
import kotlin.test.Test


@Suppress("unused")
object CommandRequestTest : JsonTest() {

	@Test
	fun test() = test(
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
