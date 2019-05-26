package tests

import io.ktor.client.engine.*
import io.ktor.client.engine.mock.*
import io.ktor.content.*
import io.ktor.http.*
import io.ktor.util.*
import team.genki.chotto.client.*
import team.genki.chotto.core.*
import kotlin.test.*


@Suppress("unused")
object CommandTest {

	@Test
	fun testMinimum() = suspendingTest {
		test(CommandTestData(
			accessToken = null,
			command = TestCommand(
				property = "value"
			),
			serializedRequest = """{
				"meta": {
					"property": "value"
				},
				"commandName": "test",
				"command": {
					"property": "value"
				}
			}""",
			serializedResponse = """{
				"status": "success",
				"response": {
					"meta": {},
					"result": {
						"property": "value"
					}
				}
			}""",
			response = CommandResponse(
				entities = emptyMap(),
				meta = TestCommandResponseMeta(
					property = null
				),
				result = TestCommandResult(
					property = "value"
				)
			)
		))
	}


	@Test
	fun testMaximum() = suspendingTest {
		test(CommandTestData(
			accessToken = AccessToken("secret access token"),
			command = TestCommand(
				property = "value"
			),
			serializedRequest = """{
				"meta": {
					"property": "value"
				},
				"commandName": "test",
				"command": {
					"property": "value"
				}
			}""",
			serializedResponse = """{
				"status": "success",
				"response": {
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
				result = TestCommandResult(
					property = "value"
				)
			)
		))
	}


	@UseExperimental(InternalAPI::class)
	private suspend fun test(data: CommandTestData<*>) {
		val client = ChottoClient(
			baseUrl = Url("https://unit.testing.local/"),
			httpEngine = MockEngine.config {
				addHandler { request ->
					assertEquals(request.url.toString(), "https://unit.testing.local/test", "HTTP request URL")
					assertEquals(request.method, HttpMethod.Post, "HTTP request method")
					assertEquals("no-cache", request.headers[HttpHeaders.CacheControl], "no-cache cache control header")
					assertEquals("no-cache", request.headers[HttpHeaders.Pragma], "no-cache pragma header")

					data.accessToken?.let { expectedAccessToken ->
						assertEquals("Bearer ${expectedAccessToken.value}", request.headers[HttpHeaders.Authorization], "authorization header")
					}

					val expectedRequestStructure = try {
						TestClientModel.prettyJson.parseJson(data.serializedRequest)
					}
					catch (e: Exception) {
						throw AssertionError("Cannot parse expected request JSON:\n${data.serializedRequest}\n\n$e")
					}

					val actualRequestJson = (request.body as? TextContent)?.text ?: throw AssertionError("only TextContent supported for now")
					val actualRequestStructure = try {
						TestClientModel.prettyJson.parseJson(actualRequestJson)
					}
					catch (e: Exception) {
						throw AssertionError("Cannot parse actual request JSON:\n$actualRequestJson\n\n$e")
					}

					assertEquals(expectedRequestStructure, actualRequestStructure, "request JSON")

					respondOk(data.serializedResponse)
				}
			},
			model = TestClientModel
		)
		assertEquals(
			data.response,
			client.execute(
				accessToken = data.accessToken,
				command = data.command
			),
			"response value"
		)
	}


	private class CommandTestData<Result : Any>(
		val accessToken: AccessToken?,
		val command: TypedCommand<*, Result>,
		val serializedRequest: String,
		val serializedResponse: String,
		val response: CommandResponse<Result, TestCommandResponseMeta>
	)
}
