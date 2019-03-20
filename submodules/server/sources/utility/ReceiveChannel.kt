package team.genki.chotto.server

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.produce


fun <E> Iterable<E>.toChannel() =
	GlobalScope.produce { forEach { send(it) } }


fun <E> CoroutineScope.emptyReceiveChannel() =
	produce<E> { }
