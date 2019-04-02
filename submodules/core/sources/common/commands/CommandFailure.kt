package team.genki.chotto.core


class CommandFailure(
	val code: String,
	val userMessage: String,
	val developerMessage: String = userMessage,
	cause: Throwable? = null
) : Exception(developerMessage, cause) {

	override val message
		get() = "[$code] $developerMessage"


	companion object {

		const val genericUserMessage = "Looks like we're having some trouble right now.\nPlease try again soon."
	}
}


internal fun CommandFailure.Companion.invalidResponse(response: String, cause: Throwable? = null) =
	CommandFailure(
		code = "invalidResponse",
		userMessage = genericUserMessage,
		developerMessage = "Server returned an invalid response: $response",
		cause = cause
	)
