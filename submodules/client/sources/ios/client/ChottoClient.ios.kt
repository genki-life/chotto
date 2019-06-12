package team.genki.chotto.client

import io.ktor.client.engine.*
import io.ktor.client.engine.ios.*
import io.ktor.http.*
import kotlinx.coroutines.*
import platform.darwin.*
import team.genki.chotto.core.*
import kotlin.coroutines.*
import kotlin.native.concurrent.*


@ThreadLocal
private lateinit var localExecutor: CommandExecutor


@Suppress("NAME_SHADOWING")
actual class ChottoClient<TModel : ClientModel<*, *>> actual constructor(
	baseUrl: Url,
	httpEngine: HttpClientEngineFactory<*>,
	private val model: TModel
) {

	private val worker = Worker.start()


	init {
		worker.execute(
			TransferMode.UNSAFE, // https://github.com/JetBrains/kotlin-native/issues/3061
			{
				CommandExecutor.Configuration(
					baseUrl = baseUrl,
					httpEngine = httpEngine,
					model = model
				)
			},
			{ localExecutor = CommandExecutor(it) }
		)
	}


	fun execute(
		accessToken: AccessToken?,
		command: TypedCommand<*, *>,
		callback: (response: CommandResponse<*, *>?, failure: CommandFailure?) -> Unit
	): () -> Unit {
		val request = CommandRequest(
			command = command,
			meta = model.createRequestMetaForCommand(command)
		).freeze()

		var jobFuture: Future<Job>? = worker.execute(
			TransferMode.UNSAFE, // https://github.com/JetBrains/kotlin-native/issues/3061
			{ Triple(accessToken, request, callback) }
		) { (accessToken, request, callback) ->
			GlobalScope.launch(WorkerDispatcher(Worker.current!!)) {
				var response: CommandResponse<*, *>? = null
				var failure: CommandFailure? = null

				try {
					response = localExecutor.execute(accessToken = accessToken, request = request)
				}
				catch (e: CancellationException) {
					// as you wish
					return@launch
				}
				catch (e: CommandFailure) {
					failure = e
				}
				catch (e: IosHttpRequestException) {
					failure = CommandFailure( // FIXME
						code = "internal",
						userMessage = e.origin?.localizedDescription ?: "An unknown error occurred."
					)
				}
				catch (e: Throwable) { // yup, Throwable - see https://github.com/ktorio/ktor/blob/e0525a274d2c9958778fb649df39d59c44341b2b/ktor-client/ktor-client-ios/darwin/src/io/ktor/client/engine/ios/Utils.kt#L27
					failure = CommandFailure( // FIXME
						code = "internal",
						userMessage = e.message ?: "An unknown error occurred."
					)
				}

				dispatch_async(dispatch_get_main_queue(), {
					callback(response, failure)
				}.freeze())
			}
		}

		return {
			jobFuture?.let { detachedJobFuture ->
				jobFuture = null
				worker.execute(TransferMode.SAFE, { detachedJobFuture }, { it.result.cancel() })
			}
		}
	}


	actual companion object {

		actual val defaultHttpEngine: HttpClientEngineFactory<*> get() = Ios
	}
}


private class WorkerDispatcher(private val worker: Worker) : CoroutineDispatcher() {

	override fun dispatch(context: CoroutineContext, block: Runnable) {
		worker.execute(TransferMode.UNSAFE, { block }, { it.run() }) // TODO find a better way than UNSAFE
	}


	override fun isDispatchNeeded(context: CoroutineContext) =
		worker != Worker.current
}
