package team.genki.chotto.core


enum class DayOfWeek {

	monday,
	tuesday,
	wednesday,
	thursday,
	friday,
	saturday,
	sunday
}


expect fun DayOfWeek.displayName(locale: Locale): String


operator fun DayOfWeek.minus(days: Int) =
	this + -days


operator fun DayOfWeek.plus(days: Int) =
	DayOfWeek.values().let { values -> values[(ordinal + days) % values.size] }
