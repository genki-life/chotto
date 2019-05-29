package team.genki.chotto.client

import kotlinx.coroutines.*
import platform.darwin.*
import team.genki.chotto.core.*
import kotlin.coroutines.*


// FIXME rework
@Suppress("UNCHECKED_CAST")
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
