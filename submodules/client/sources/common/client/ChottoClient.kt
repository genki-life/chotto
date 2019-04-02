package team.genki.chotto.client

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.utils.CacheControl
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.Url
import io.ktor.http.content.TextContent
import team.genki.chotto.core.*


class ChottoClient<TModel : ClientModel<*, *>>(
	baseUrl: Url,
	private val httpClient: HttpClient,
	private val model: TModel
) {

	private val endpointUrl = baseUrl.toBuilder().appendPath(model.name).build()


	@Suppress("UNCHECKED_CAST")
	private suspend fun <TResult : Any> internalExecute(
		accessToken: AccessToken?,
		request: CommandRequest<Command.Typed<*, TResult>, *>
	): CommandResponse<TResult, CommandResponse.Meta> {
		val rawResponse = httpClient.post<String>(endpointUrl) {
			body = TextContent(
				text = model.jsonConverter.serializeCommandRequest(request as CommandRequest<Command.Typed<*, *>, Nothing>),
				contentType = ContentType.Application.Json
			)

			accessToken?.let { header(HttpHeaders.Authorization, "Bearer ${it.value}") }
			header(HttpHeaders.CacheControl, CacheControl.NO_CACHE)
			header(HttpHeaders.Pragma, "no-cache")
		}

		return model.jsonConverter.parseCommandResponse(rawResponse, request.command)
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
