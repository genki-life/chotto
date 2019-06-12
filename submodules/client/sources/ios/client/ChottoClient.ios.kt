package team.genki.chotto.client

import io.ktor.client.engine.ios.*
import io.ktor.http.*
import kotlinx.coroutines.*
import team.genki.chotto.core.*
import kotlin.coroutines.*
import kotlin.native.concurrent.*


@ThreadLocal
private lateinit var localExecutor: CommandExecutor


@Suppress("NAME_SHADOWING")
actual class ChottoClient<TModel : ClientModel<*, *>> actual constructor(
	baseUrl: Url,
	private val model: TModel
) {

	private val worker = Worker.start()


	init {
		worker.execute(
			TransferMode.UNSAFE, // https://github.com/JetBrains/kotlin-native/issues/3061
			{
				CommandExecutor.Configuration(
					baseUrl = baseUrl,
					httpEngine = Ios,
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
		callback.freeze() // FIXME needed?

		val request = CommandRequest(
			command = command,
			meta = model.createRequestMetaForCommand(command)
		)

		worker.execute(
			TransferMode.UNSAFE, // https://github.com/JetBrains/kotlin-native/issues/3061
			{ Triple(accessToken, request, callback) }
		) { (accessToken, request, callback) ->
			GlobalScope.launch(WorkerDispatcher(Worker.current!!)) {
				val response = localExecutor.execute(accessToken = accessToken, request = request).freeze()

				callback(response, null) // FIXME main thread
			}
		}

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
		return {}
	}


	actual companion object
}


private class WorkerDispatcher(private val worker: Worker) : CoroutineDispatcher() {

	override fun dispatch(context: CoroutineContext, block: Runnable) {
		worker.execute(TransferMode.UNSAFE, { block }, { it.run() }) // TODO find a better way than UNSAFE
	}


	override fun isDispatchNeeded(context: CoroutineContext) =
		worker != Worker.current
}
