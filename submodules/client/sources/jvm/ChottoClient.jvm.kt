package team.genki.chotto.client

import io.ktor.client.engine.apache.*
import io.ktor.http.*
import team.genki.chotto.core.*


@Suppress("NAME_SHADOWING")
actual class ChottoClient<TModel : ClientModel<*, *>> actual constructor(
	baseUrl: Url,
	private val model: TModel
) {

	private val executor = CommandExecutor(CommandExecutor.Configuration(
		baseUrl = baseUrl,
		httpEngine = Apache,
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

		return executor.execute(accessToken = accessToken, request = request)

		// FIXME handle exceptions and cancellation
//		val x = BackgroundWorkerDispatcher.worker.execute(
//			TransferMode.SAFE,
//			{ Pair(this, Triple(accessToken, command, callback)) }
//		) { (client, p) ->
//			println("a")
//			val (accessToken, command, callback) = p
//
//			val job = GlobalScope.launch(Dispatchers.Unconfined) {
//				println("b")
//				try {
//					val response = client.unsafeExecute(
//						accessToken = accessToken,
//						command = command
//					)
//					println("c")
//
//					callback(response, null)
//					println("d")
//				}
//				catch (e: CancellationException) {
//					// as you wish
//				}
//				catch (e: CommandFailure) {
//					println("e")
//					callback(null, e.freeze())
//					println("f")
//				}
//				catch (e: IosHttpRequestException) {
//					println("g")
//					callback(null, CommandFailure( // FIXME
//						code = "internal",
//						userMessage = e.origin?.localizedDescription ?: "An unknown error occurred."
//					).freeze())
//					println("h")
//				}
//				catch (e: Throwable) { // yup, Throwable - see https://github.com/ktorio/ktor/blob/e0525a274d2c9958778fb649df39d59c44341b2b/ktor-client/ktor-client-ios/darwin/src/io/ktor/client/engine/ios/Utils.kt#L27
//					println("i")
//					callback(null, CommandFailure( // FIXME
//						code = "internal",
//						userMessage = e.message ?: "An unknown error occurred."
//					).freeze())
//					println("j")
//				}
//			}
//
//			println("x")
//			return@execute 2// { job.cancel() }.freeze()
//		}.result
//		println("y")
	}


	actual companion object
}
