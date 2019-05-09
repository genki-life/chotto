package team.genki.chotto.core

import com.github.fluidsonic.fluid.stdlib.*
import java.util.Currency as JavaCurrency


actual val allCurrencyCodes: Set<String> = JavaCurrency.getAvailableCurrencies().mapTo(hashSetOf()) { it.currencyCode }


actual fun Currency.name(locale: Locale): String =
	JavaCurrency.getInstance(code).getDisplayName(locale.platform)
