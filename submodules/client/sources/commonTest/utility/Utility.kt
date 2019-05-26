package tests

import kotlinx.coroutines.*


internal expect fun suspendingTest(block: suspend CoroutineScope.() -> Unit)
