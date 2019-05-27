package team.genki.chotto.client

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.request.*
import io.ktor.client.utils.CacheControl
import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.serialization.*
import team.genki.chotto.core.*


class ChottoClient<TModel : ClientModel<*, *>>(
	baseUrl: Url,
	httpEngine: HttpClientEngineFactory<*>,
	internal val model: TModel
) {

	private val endpointUrl = baseUrl.toBuilder().appendPath(model.name).build()
	private val httpClient = HttpClient(httpEngine) {
		expectSuccess = false
	}


	@Suppress("UNCHECKED_CAST")
	internal suspend fun internalExecute(
		accessToken: AccessToken?,
		request: CommandRequest<*, *>
	): CommandResponse<*, *> {
		val rawResponse = httpClient.post<String>(endpointUrl) {
			val requestSerializer = CommandRequest.serializer(request.command.definition.serializer, model.commandRequestMetaSerializer)
				as KSerializer<CommandRequest<*, *>>

			body = TextContent(
				text = model.json.stringify(requestSerializer, request),
				contentType = ContentType.Application.Json
			)

			accessToken?.let { header(HttpHeaders.Authorization, "Bearer ${it.value}") }
			header(HttpHeaders.CacheControl, CacheControl.NO_CACHE)
			header(HttpHeaders.Pragma, "no-cache")
		}

		val status = model.json.parse(
			deserializer = CommandRequestStatus.serializer(request.command.definition.resultSerializer, model.commandResponseMetaSerializer),
			string = rawResponse
		)

		when (status) {
			is CommandRequestStatus.Failure -> throw status.cause
			is CommandRequestStatus.Success<*, *> -> return status.response
		}
	}
}


@Suppress("UNCHECKED_CAST")
suspend fun ChottoClient<*>.unsafeExecute(
	accessToken: AccessToken?,
	command: TypedCommand<*, *>
): CommandResponse<*, *> =
	internalExecute(
		accessToken = accessToken,
		request = CommandRequest(
			command = command,
			meta = model.createRequestMetaForCommand(command)
		)
	)


@Suppress("UNCHECKED_CAST")
suspend fun <
	TModel : ClientModel<*, TCommandResponseMeta>,
	TCommandResponseMeta : CommandResponseMeta,
	TResult : Any
	>
	ChottoClient<TModel>.execute(
	accessToken: AccessToken?,
	command: TypedCommand<*, TResult>
): CommandResponse<TResult, TCommandResponseMeta> =
	internalExecute(
		accessToken = accessToken,
		request = CommandRequest(
			command = command,
			meta = model.createRequestMetaForCommand(command)
		)
	) as CommandResponse<TResult, TCommandResponseMeta>
