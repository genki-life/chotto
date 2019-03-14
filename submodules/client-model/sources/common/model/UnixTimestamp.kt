package team.genki.chotto.client.model


data /*inline*/ class UnixTimestamp(val seconds: Long) {

	override fun toString() = seconds.toString()
}
