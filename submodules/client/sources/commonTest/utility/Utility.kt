package tests

import kotlinx.coroutines.*


internal expect fun <R> suspending(block: suspend CoroutineScope.() -> R): R
