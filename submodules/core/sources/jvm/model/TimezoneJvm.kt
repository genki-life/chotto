package team.genki.chotto.core

import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Locale as JavaLocale


actual class Timezone(
	val java: ZoneId
) {

	actual val id: String get() = java.id


	actual companion object {

		actual val utc = Timezone(ZoneOffset.UTC)


		actual fun byId(id: String) =
			ZoneId.of(id)?.let(::Timezone)
	}
}
