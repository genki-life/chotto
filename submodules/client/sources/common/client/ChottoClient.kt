package team.genki.chotto.client

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.request.*
import io.ktor.client.utils.CacheControl
import io.ktor.http.*
import io.ktor.http.content.*
import team.genki.chotto.core.*


class ChottoClient<TModel : ClientModel<TCommandRequestMeta, *>, TCommandRequestMeta : CommandRequestMeta>(
	baseUrl: Url,
	httpEngine: HttpClientEngineFactory<*>,
	private val model: TModel
) {

	private val endpointUrl = baseUrl.toBuilder().appendPath(model.name).build()
	private val httpClient = HttpClient(httpEngine) {
		expectSuccess = false
	}


	@Suppress("UNCHECKED_CAST")
	private suspend fun <TResult : Any, TCommand : TypedCommand<TCommand, TResult>> internalExecute(
		accessToken: AccessToken?,
		request: CommandRequest<TCommand, TCommandRequestMeta>
	): CommandResponse<TResult, CommandResponseMeta> {
		val rawResponse = httpClient.post<String>(endpointUrl) {
			body = TextContent(
				text = model.json.stringify(CommandRequest.serializer(request.command.definition.serializer, model.commandRequestMetaSerializer), request),
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
			is CommandRequestStatus.Success<*, *> -> return status.response as CommandResponse<TResult, CommandResponseMeta>
		}
	}


	internal suspend fun <TCommand : TypedCommand<TCommand, TResult>, TResult : Any> internalExecute(
		accessToken: AccessToken?,
		command: TCommand
	): CommandResponse<TResult, CommandResponseMeta> =
		internalExecute(
			accessToken = accessToken,
			request = CommandRequest(
				command = command,
				meta = model.createRequestMetaForCommand(command)
			)
		)
}


@Suppress("UNCHECKED_CAST")
suspend fun <
	TModel : ClientModel<TCommandRequestMeta, TCommandResponseMeta>,
	TCommand : TypedCommand<TCommand, TResult>,
	TCommandRequestMeta : CommandRequestMeta,
	TCommandResponseMeta : CommandResponseMeta,
	TResult : Any
	> ChottoClient<TModel, TCommandRequestMeta>.execute(
	accessToken: AccessToken?,
	command: TCommand
): CommandResponse<TResult, TCommandResponseMeta> =
	internalExecute(accessToken = accessToken, command = command) as CommandResponse<TResult, TCommandResponseMeta>
