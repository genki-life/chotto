package team.genki.chotto.client.connector

import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.encodeURLQueryComponent
import io.ktor.http.takeFrom


fun Url.toBuilder() =
	URLBuilder().takeFrom(this)


fun URLBuilder.appendPath(segments: List<String>) = apply {
	encodedPath += segments.joinToString(
		separator = "/",
		prefix = if (encodedPath.endsWith('/')) "" else "/"
	) { it.encodeURLQueryComponent() }
}


fun URLBuilder.appendPath(vararg segments: String) =
	appendPath(segments.toList())
