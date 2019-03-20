package team.genki.chotto.core


data /*inline*/ class UnixTimestamp(val seconds: Long) {

	override fun toString() = seconds.toString()
}
