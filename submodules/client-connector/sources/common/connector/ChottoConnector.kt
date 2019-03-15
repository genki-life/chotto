package team.genki.chotto.client.connector

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.http.Url
import kotlin.reflect.KClass
import team.genki.chotto.client.model.*


class ChottoConnector<CommandRequestMeta : CommandRequest.Meta, CommandResponseMeta : CommandResponse.Meta>(
	baseUrl: Url,
	private val httpClient: HttpClient,
	private val model: ClientModel<CommandRequestMeta, CommandResponseMeta>
) {

	private val commandUrl = baseUrl.toBuilder().appendPath("commands").build()


	private suspend fun <Result : Any> execute(request: CommandRequest<Command<Result>, CommandRequestMeta>): CommandResponse<Result, CommandResponseMeta> {
		val rawResponse = httpClient.post<String>(commandUrl) {
			body = model.jsonConverter.serializeCommandRequest(request)
		}

		return model.jsonConverter.parseCommandResponse(rawResponse, request.command)
	}


	suspend fun <Result : Any> execute(command: Command<Result>): CommandResponse<Result, CommandResponseMeta> =
		execute(request = CommandRequest(
			command = command,
			meta = model.createRequestMetaForCommand(command)
		))
}
