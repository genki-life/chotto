package team.genki.chotto.client

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.request.*
import io.ktor.client.utils.CacheControl
import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import team.genki.chotto.core.*


internal class CommandExecutor(
	configuration: Configuration
) {

	private val commandRequestMetaSerializer = configuration.commandRequestMetaSerializer
	private val commandResponseMetaSerializer = configuration.commandResponseMetaSerializer
	private val endpointUrl = configuration.endpointUrl
	private val httpClient = configuration.httpClient
	private val json = configuration.json


	@Suppress("UNCHECKED_CAST")
	suspend fun execute(
		accessToken: AccessToken?,
		request: CommandRequest<*, *>
	): CommandResponse<*, *> {
		val rawResponse = httpClient.post<ByteArray>(endpointUrl) {
			val requestSerializer = CommandRequest.serializer(request.command.definition.serializer, commandRequestMetaSerializer)
				as KSerializer<CommandRequest<*, *>>

			body = ByteArrayContent(
				bytes = encodeString(json.stringify(requestSerializer, request)),
				contentType = ContentType.Application.Json
			)

			accessToken?.let { header(HttpHeaders.Authorization, "Bearer ${it.value}") }
			header(HttpHeaders.CacheControl, CacheControl.NO_CACHE)
			header(HttpHeaders.Pragma, "no-cache")
		}

		@Suppress("MoveVariableDeclarationIntoWhen")
		val status = json.parse(
			deserializer = CommandRequestStatus.serializer(request.command.definition.resultSerializer, commandResponseMetaSerializer),
			string = decodeString(rawResponse)
		)

		when (status) {
			is CommandRequestStatus.Failure -> throw status.cause
			is CommandRequestStatus.Success<*, *> -> return status.response
		}
	}


	class Configuration private constructor(
		val commandRequestMetaSerializer: KSerializer<out CommandRequestMeta>,
		val commandResponseMetaSerializer: KSerializer<out CommandResponseMeta>,
		val endpointUrl: Url,
		val httpClient: HttpClient,
		val json: Json
	) {

		constructor(baseUrl: Url, httpEngine: HttpClientEngineFactory<*>, model: ClientModel<*, *>) :
			this(
				commandRequestMetaSerializer = model.commandRequestMetaSerializer,
				commandResponseMetaSerializer = model.commandResponseMetaSerializer,
				endpointUrl = baseUrl.toBuilder().appendPath(model.name).build(),
				httpClient = HttpClient(httpEngine) {
					expectSuccess = false
				},
				json = model.json
			)
	}
}


// https://github.com/Kotlin/kotlinx-io/issues/53

internal expect fun CommandExecutor.decodeString(data: ByteArray): String

internal expect fun CommandExecutor.encodeString(string: String): ByteArray
