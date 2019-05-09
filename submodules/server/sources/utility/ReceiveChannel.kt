package team.genki.chotto.server

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*


fun <E> Iterable<E>.toChannel() =
	GlobalScope.produce { forEach { send(it) } }


fun <E> CoroutineScope.emptyReceiveChannel() =
	produce<E> { }
