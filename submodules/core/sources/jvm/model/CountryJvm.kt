package team.genki.chotto.core

import com.github.fluidsonic.fluid.stdlib.*
import java.util.Locale as JavaLocale


actual val allCountryCodes: Set<String> = JavaLocale.getISOCountries().toSet()


actual fun Country.name(locale: Locale): String =
	JavaLocale("", code).getDisplayCountry(locale.platform)
