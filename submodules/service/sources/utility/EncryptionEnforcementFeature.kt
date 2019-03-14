package team.genki.chotto

import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.ApplicationFeature
import io.ktor.application.call
import io.ktor.features.origin
import io.ktor.util.AttributeKey


internal object EncryptionEnforcementFeature : ApplicationFeature<ApplicationCallPipeline, Unit, Unit> {

	override val key = AttributeKey<Unit>("Chotto: encryption enforcement feature")


	override fun install(pipeline: ApplicationCallPipeline, configure: Unit.() -> Unit) {
		Unit.configure()

		pipeline.intercept(ApplicationCallPipeline.Features) {
			if (call.request.origin.scheme != "https")
				throw ChottoCommandFailure(
					code = "encryptedConnectionRequired",
					userMessage = ChottoCommandFailure.genericUserMessage,
					developerMessage = "This API must only be used over an encrypted connection."
				)
		}
	}
}
