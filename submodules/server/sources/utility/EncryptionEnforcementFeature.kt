package team.genki.chotto.server

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.util.*
import team.genki.chotto.core.*


internal object EncryptionEnforcementFeature : ApplicationFeature<ApplicationCallPipeline, Unit, Unit> {

	override val key = AttributeKey<Unit>("Chotto: encryption enforcement feature")


	override fun install(pipeline: ApplicationCallPipeline, configure: Unit.() -> Unit) {
		Unit.configure()

		pipeline.intercept(ApplicationCallPipeline.Features) {
			if (call.request.origin.scheme != "https")
				throw CommandFailure(
					code = "encryptedConnectionRequired",
					userMessage = CommandFailure.genericUserMessage,
					developerMessage = "This API must only be used over an encrypted connection."
				)
		}
	}
}
