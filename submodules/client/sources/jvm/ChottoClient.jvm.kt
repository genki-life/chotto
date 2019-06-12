package team.genki.chotto.client

import io.ktor.client.engine.*
import io.ktor.client.engine.apache.*
import io.ktor.http.*
import kotlinx.coroutines.*
import team.genki.chotto.core.*


@Suppress("NAME_SHADOWING")
actual class ChottoClient<TModel : ClientModel<*, *>> actual constructor(
	baseUrl: Url,
	httpEngine: HttpClientEngineFactory<*>,
	private val model: TModel
) {

	private val executor = CommandExecutor(CommandExecutor.Configuration(
		baseUrl = baseUrl,
		httpEngine = httpEngine,
		model = model
	))


	@Suppress("UNCHECKED_CAST")
	suspend fun <
		TModel : ClientModel<*, TCommandResponseMeta>,
		TCommandResponseMeta : CommandResponseMeta,
		TResult : Any
		>
		execute(
		accessToken: AccessToken?,
		command: TypedCommand<*, TResult>
	): CommandResponse<TResult, TCommandResponseMeta> =
		unsafeExecute(
			accessToken = accessToken,
			command = command
		) as CommandResponse<TResult, TCommandResponseMeta>


	suspend fun unsafeExecute(
		accessToken: AccessToken?,
		command: TypedCommand<*, *>
	): CommandResponse<*, *> {
		val request = CommandRequest(
			command = command,
			meta = model.createRequestMetaForCommand(command)
		)

		try {
			return executor.execute(accessToken = accessToken, request = request)
		}
		catch (e: CancellationException) {
			throw e
		}
		catch (e: CommandFailure) {
			throw e
		}
		catch (e: Throwable) { // yup, Throwable - see https://github.com/ktorio/ktor/blob/e0525a274d2c9958778fb649df39d59c44341b2b/ktor-client/ktor-client-ios/darwin/src/io/ktor/client/engine/ios/Utils.kt#L27
			throw CommandFailure( // FIXME
				code = "internal",
				userMessage = e.message ?: "An unknown error occurred."
			)
		}
	}


	actual companion object {

		internal actual val defaultHttpEngine: HttpClientEngineFactory<*> get() = Apache
	}
}
