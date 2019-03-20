package tests

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking


internal actual fun <R> suspending(block: suspend CoroutineScope.() -> R): R = runBlocking(block = block)
