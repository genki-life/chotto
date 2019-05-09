package team.genki.chotto.core


data /*inline*/ class EmailAddress(val value: String) {

	fun toLowerCase() =
		EmailAddress(value.toLowerCase())


	override fun toString() = value


	companion object
}
