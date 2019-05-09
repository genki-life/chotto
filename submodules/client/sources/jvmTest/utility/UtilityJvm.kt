package tests

import kotlinx.coroutines.*


internal actual fun <R> suspending(block: suspend CoroutineScope.() -> R): R = runBlocking(block = block)
