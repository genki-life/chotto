package team.genki.chotto.client

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.http.*
import team.genki.chotto.core.*


class ChottoClient<TModel : ClientModel<*, *>>(
	baseUrl: Url,
	httpEngine: HttpClientEngineFactory<*>,
	private val model: TModel
) {

	private val endpointUrl = baseUrl.toBuilder().appendPath(model.name).build()
	private val httpClient = HttpClient(httpEngine) {
		expectSuccess = false
	}


	@Suppress("UNCHECKED_CAST")
	private suspend fun <TResult : Any> internalExecute(
		accessToken: AccessToken?,
		request: CommandRequest<Command.Typed<*, TResult>, *>
	): CommandResponse<TResult, CommandResponse.Meta> {
		TODO() // FIXME
//		val rawResponse = httpClient.post<String>(endpointUrl) {
//			body = TextContent(
//				text = model.jsonConverter.serializeCommandRequest(request as CommandRequest<Command.Typed<*, *>, Nothing>),
//				contentType = ContentType.Application.Json
//			)
//
//			accessToken?.let { header(HttpHeaders.Authorization, "Bearer ${it.value}") }
//			header(HttpHeaders.CacheControl, CacheControl.NO_CACHE)
//			header(HttpHeaders.Pragma, "no-cache")
//		}
//
//		return model.jsonConverter.parseCommandResponse(rawResponse, request.command)
	}


	internal suspend fun <TResult : Any> internalExecute(
		accessToken: AccessToken?,
		command: Command.Typed<*, TResult>
	): CommandResponse<TResult, CommandResponse.Meta> =
		internalExecute(
			accessToken = accessToken,
			request = CommandRequest(
				command = command,
				meta = model.createRequestMetaForCommand(command)
			)
		)
}


@Suppress("UNCHECKED_CAST")
suspend fun <TModel : ClientModel<*, TCommandResponseMeta>, TCommandResponseMeta : CommandResponse.Meta, TResult : Any> ChottoClient<TModel>.execute(
	accessToken: AccessToken?,
	command: Command.Typed<*, TResult>
): CommandResponse<TResult, TCommandResponseMeta> =
	internalExecute(accessToken = accessToken, command = command) as CommandResponse<TResult, TCommandResponseMeta>
