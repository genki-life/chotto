package team.genki.chotto.core

import java.time.Instant


actual typealias Timestamp = Instant


actual fun Timestamp(unixTimestamp: UnixTimestamp): Timestamp =
	Instant.ofEpochSecond(unixTimestamp.seconds)


actual val Timestamp.unixTimestamp
	get() = UnixTimestamp(epochSecond)
