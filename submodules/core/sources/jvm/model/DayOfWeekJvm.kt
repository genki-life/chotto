package team.genki.chotto.core

import java.time.format.TextStyle
import java.time.DayOfWeek as JavaDayOfWeek


val DayOfWeek.java // FIXME rework
	get() = when (this) {
		DayOfWeek.monday -> JavaDayOfWeek.MONDAY
		DayOfWeek.tuesday -> JavaDayOfWeek.TUESDAY
		DayOfWeek.wednesday -> JavaDayOfWeek.WEDNESDAY
		DayOfWeek.thursday -> JavaDayOfWeek.THURSDAY
		DayOfWeek.friday -> JavaDayOfWeek.FRIDAY
		DayOfWeek.saturday -> JavaDayOfWeek.SATURDAY
		DayOfWeek.sunday -> JavaDayOfWeek.SUNDAY
	}


val JavaDayOfWeek.model // FIXME rework
	get() = when (this) {
		JavaDayOfWeek.MONDAY -> DayOfWeek.monday
		JavaDayOfWeek.TUESDAY -> DayOfWeek.tuesday
		JavaDayOfWeek.WEDNESDAY -> DayOfWeek.wednesday
		JavaDayOfWeek.THURSDAY -> DayOfWeek.thursday
		JavaDayOfWeek.FRIDAY -> DayOfWeek.friday
		JavaDayOfWeek.SATURDAY -> DayOfWeek.saturday
		JavaDayOfWeek.SUNDAY -> DayOfWeek.sunday
	}


actual fun DayOfWeek.displayName(locale: Locale): String =
	java.getDisplayName(TextStyle.FULL_STANDALONE, locale.java)
