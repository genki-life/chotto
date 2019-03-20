package team.genki.chotto.core


class CommandFailure(
	val code: String,
	val userMessage: String,
	val developerMessage: String = userMessage,
	cause: Throwable? = null
) : Exception(developerMessage, cause) {

	companion object {

		const val genericUserMessage = "Looks like we're having some trouble right now.\nPlease try again soon."
	}
}
