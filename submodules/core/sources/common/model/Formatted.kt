package team.genki.chotto.core


data class Formatted<out Value : Any, out FormattedValue : Any>(
	val raw: Value,
	val formatted: FormattedValue
)
