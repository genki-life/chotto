package team.genki.chotto.client.model


expect class Timestamp

expect fun Timestamp(unixTimestamp: UnixTimestamp): Timestamp
expect val Timestamp.unixTimestamp: UnixTimestamp


fun Timestamp(unixTimestamp: Long) =
	Timestamp(UnixTimestamp(unixTimestamp))
