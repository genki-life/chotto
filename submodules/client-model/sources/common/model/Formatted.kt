package team.genki.chotto.client.model


data class Formatted<out Value : Any, out FormattedValue : Any>(
	val raw: Value,
	val formatted: FormattedValue
)
