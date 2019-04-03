package team.genki.chotto.core

import java.time.format.TextStyle
import java.time.DayOfWeek as JavaDayOfWeek


fun JavaDayOfWeek.toChotto() = when (this) {
	JavaDayOfWeek.MONDAY -> DayOfWeek.monday
	JavaDayOfWeek.TUESDAY -> DayOfWeek.tuesday
	JavaDayOfWeek.WEDNESDAY -> DayOfWeek.wednesday
	JavaDayOfWeek.THURSDAY -> DayOfWeek.thursday
	JavaDayOfWeek.FRIDAY -> DayOfWeek.friday
	JavaDayOfWeek.SATURDAY -> DayOfWeek.saturday
	JavaDayOfWeek.SUNDAY -> DayOfWeek.sunday
}


fun DayOfWeek.toJava() = when (this) {
	DayOfWeek.monday -> JavaDayOfWeek.MONDAY
	DayOfWeek.tuesday -> JavaDayOfWeek.TUESDAY
	DayOfWeek.wednesday -> JavaDayOfWeek.WEDNESDAY
	DayOfWeek.thursday -> JavaDayOfWeek.THURSDAY
	DayOfWeek.friday -> JavaDayOfWeek.FRIDAY
	DayOfWeek.saturday -> JavaDayOfWeek.SATURDAY
	DayOfWeek.sunday -> JavaDayOfWeek.SUNDAY
}


actual fun DayOfWeek.displayName(locale: Locale): String =
	toJava().getDisplayName(TextStyle.FULL_STANDALONE, locale.java)
