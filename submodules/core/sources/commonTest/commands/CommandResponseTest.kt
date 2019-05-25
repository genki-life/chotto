package tests


//object CommandResponseTest {
//
//	@Test
//	fun test() = assertJsonSerialization(
//		value = CommandResponse(
//			entities = mapOf<EntityId, Entity>(
//				TestId("1") to TestEntity(
//					id = TestId("1"),
//					property = "value"
//				)
//			),
//			meta = TestCommandResponseMeta(
//				property = "value"
//			),
//			result = TestCommand.Result(
//				property = "value"
//			)
//		),
//		json = """{
//			"entities": {
//				"tests/1": {
//					"id":       "tests/1",
//					"property": "value"
//				}
//			},
//			"meta": {
//				"property": "value"
//			},
//			"result": {
//				"property": "value"
//			}
//		}"""
//	)
//}
