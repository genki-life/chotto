package tests

import team.genki.chotto.core.*
import kotlin.test.Test


@Suppress("unused")
object CommandResponseTest : JsonTest() {

	@Test
	fun test() = test(
		value = CommandResponse(
			entities = mapOf<EntityId, Entity>(
				TestId("1") to TestEntity(
					id = TestId("1"),
					property = "value"
				)
			),
			meta = TestCommandResponseMeta(
				property = "value"
			),
			result = TestCommand.Result(
				property = "value"
			)
		),
		json = """{
			"entities": {
				"tests/1": {
					"id":       "tests/1",
					"property": "value"
				}
			},
			"meta": {
				"property": "value"
			},
			"result": {
				"property": "value"
			}
		}"""
	)
}
