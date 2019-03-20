package team.genki.chotto.core

import java.util.Locale as JavaLocale


actual class Locale(
	val java: JavaLocale
) {

	actual companion object {

		actual val englishInUnitedStates = Locale(JavaLocale.US)
	}
}
