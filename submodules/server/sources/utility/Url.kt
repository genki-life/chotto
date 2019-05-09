package team.genki.chotto.server

import io.ktor.http.*


fun Url.toBuilder() =
	URLBuilder().takeFrom(this)
