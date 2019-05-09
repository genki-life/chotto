package team.genki.chotto.client

import io.ktor.http.*


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
