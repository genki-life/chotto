package tests

import team.genki.chotto.core.*
import kotlin.test.*


object CommandResponseTest {

	@Test
	fun test() = assertJsonSerialization(
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
			result = TestCommandResult(
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
		}""",
		serializer = CommandResponse.serializer(TestCommandResult.serializer(), TestCommandResponseMeta.serializer())
	)
}
