package team.genki.chotto.server

import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.takeFrom


fun Url.toBuilder() =
	URLBuilder().takeFrom(this)
