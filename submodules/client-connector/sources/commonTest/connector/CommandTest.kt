package tests

import team.genki.chotto.client.connector.*
import team.genki.chotto.client.model.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.responseOk
import io.ktor.content.TextContent
import io.ktor.http.HttpMethod
import io.ktor.http.Url
import kotlin.test.Test
import kotlin.test.assertEquals


@Suppress("unused")
object CommandTest {

	@Test
	fun testMinimum() = suspending {
		test(CommandTestData(
			command = TestCommand(
				property = "value"
			),
			serializedRequest = """{
				"name":    "test",
				"meta":    {
					"property": "value"
				},
				"command": {
					"property": "value"
				}
			}""",
			serializedResponse = """{
				"result": {
					"property": "value"
				}
			}""",
			response = CommandResponse(
				entities = emptyMap(),
				meta = TestCommandResponseMeta(
					property = null
				),
				result = TestCommand.Result(
					property = "value"
				)
			)
		))
	}


	@Test
	fun testMaximum() = suspending {
		test(CommandTestData(
			command = TestCommand(
				property = "value"
			),
			serializedRequest = """{
				"name":    "test",
				"meta":    {
					"property": "value"
				},
				"command": {
					"property": "value"
				}
			}""",
			serializedResponse = """{
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
			response = CommandResponse(
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
			)
		))
	}


	private suspend fun test(data: CommandTestData<*>) {
		val client = ChottoConnector(
			baseUrl = Url("https://unit.testing.local/"),
			httpClient = HttpClient(MockEngine {
				assertEquals(url.toString(), "https://unit.testing.local/commands", "HTTP request URL")
				assertEquals(method, HttpMethod.Post, "HTTP request method")

				val expectedRequestStructure = try {
					parseJson(data.serializedRequest)
				}
				catch (e: Exception) {
					throw AssertionError("Cannot parse expected request JSON:\n${data.serializedRequest}\n\n$e")
				}

				val actualRequestJson = (content as? TextContent)?.text ?: throw AssertionError("only TextContent supported for now")
				val actualRequestStructure = try {
					parseJson(actualRequestJson)
				}
				catch (e: Exception) {
					throw AssertionError("Cannot parse actual request JSON:\n$actualRequestJson\n\n$e")
				}

				assertEquals(expectedRequestStructure, actualRequestStructure, "request JSON")

				responseOk(data.serializedResponse)
			}),
			model = TestClientModel
		)
		assertEquals(
			data.response,
			client.execute(data.command),
			"response value"
		)
	}


	private class CommandTestData<Result : Any>(
		val command: Command<Result>,
		val serializedRequest: String,
		val serializedResponse: String,
		val response: CommandResponse<Result, TestCommandResponseMeta>
	)
}


expect fun CommandTest.parseJson(json: String): Any?
