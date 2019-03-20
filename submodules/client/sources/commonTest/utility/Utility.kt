package tests

import kotlinx.coroutines.CoroutineScope


internal expect fun <R> suspending(block: suspend CoroutineScope.() -> R): R
