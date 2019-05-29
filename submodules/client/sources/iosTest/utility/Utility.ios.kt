package tests

import kotlinx.coroutines.*


internal actual fun suspendingTest(block: suspend CoroutineScope.() -> Unit) =
	runBlocking(block = block)
