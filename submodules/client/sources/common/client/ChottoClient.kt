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


class ChottoClient<TCommandRequestMeta : CommandRequest.Meta, TCommandResponseMeta : CommandResponse.Meta>(
	baseUrl: Url,
	private val httpClient: HttpClient,
	private val model: ClientModel<TCommandRequestMeta, TCommandResponseMeta>
) {

	private val endpointUrl = baseUrl.toBuilder().appendPath(model.name).build()


	private suspend fun <TResult : Any> execute(request: CommandRequest<Command.Typed<*, TResult>, TCommandRequestMeta>): CommandResponse<TResult, TCommandResponseMeta> {
		val rawResponse = httpClient.post<String>(endpointUrl) {
			body = TextContent(
				text = model.jsonConverter.serializeCommandRequest(request),
				contentType = ContentType.Application.Json
			)

			header(HttpHeaders.CacheControl, CacheControl.NO_CACHE)
			header(HttpHeaders.Pragma, "no-cache")
		}

		return model.jsonConverter.parseCommandResponse(rawResponse, request.command)
	}


	suspend fun <TResult : Any> execute(command: Command.Typed<*, TResult>): CommandResponse<TResult, TCommandResponseMeta> =
		execute(request = CommandRequest(
			command = command,
			meta = model.createRequestMetaForCommand(command)
		))
}
