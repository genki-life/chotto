package team.genki.chotto.client.model


data /*inline*/ class EmailAddress(val value: String) {

	fun toLowerCase() =
		EmailAddress(value.toLowerCase())


	override fun toString() = value
}
