package team.genki.chotto.client

import io.ktor.client.engine.ios.*
import io.ktor.util.*
import kotlinx.coroutines.*
import platform.darwin.*
import team.genki.chotto.core.*
import kotlin.coroutines.*


// FIXME rework
@Suppress("UNCHECKED_CAST")
@UseExperimental(KtorExperimentalAPI::class)
fun ChottoClient<*>.execute(
	accessToken: AccessToken?,
	command: TypedCommand<*, *>,
	callback: (response: CommandResponse<*, *>?, failure: CommandFailure?) -> Unit
) {
	GlobalScope.launch(ApplicationDispatcher) {
		try {
			val response = unsafeExecute(
				accessToken = accessToken,
				command = command
			)

			callback(response, null)
		}
		catch (e: CommandFailure) {
			callback(null, e)
		}
		catch (e: IosHttpRequestException) {
			callback(null, CommandFailure( // FIXME
				code = "internal",
				userMessage = e.origin?.description ?: "An unknown error occurred."
			))
		}
		catch (e: Throwable) { // yup, Throwable - see https://github.com/ktorio/ktor/blob/e0525a274d2c9958778fb649df39d59c44341b2b/ktor-client/ktor-client-ios/darwin/src/io/ktor/client/engine/ios/Utils.kt#L27
			callback(null, CommandFailure( // FIXME
				code = "internal",
				userMessage = e.message ?: "An unknown error occurred."
			))
		}
	}
}


private val ApplicationDispatcher: CoroutineDispatcher = NsQueueDispatcher(dispatch_get_main_queue())

private class NsQueueDispatcher(private val dispatchQueue: dispatch_queue_t) : CoroutineDispatcher() {

	override fun dispatch(context: CoroutineContext, block: Runnable) {
		dispatch_async(dispatchQueue) {
			block.run()
		}
	}
}
