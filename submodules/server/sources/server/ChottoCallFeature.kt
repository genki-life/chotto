package team.genki.chotto.server

import io.ktor.application.*
import io.ktor.util.*
import io.ktor.util.pipeline.*


internal class ChottoCallFeature<Context : ChottoServerContext, Transaction : ChottoTransaction>(
	private val configuration: ServerConfiguration<Context, Transaction>
) : ApplicationFeature<ApplicationCallPipeline, Unit, Unit> {

	override val key = AttributeKey<Unit>("Chotto: call feature")


	@Suppress("UNCHECKED_CAST")
	override fun install(pipeline: ApplicationCallPipeline, configure: Unit.() -> Unit) {
		Unit.configure()

		pipeline.intercept(ApplicationCallPipeline.Setup) {
			call.attributes.put(AttributeKeys.call, configuration.createCall())
		}
	}


	object AttributeKeys {

		val call = AttributeKey<ChottoCall<*>>("Chotto: call")
	}
}


internal val ApplicationCall.chottoCall
	get() = attributes[ChottoCallFeature.AttributeKeys.call]


internal val PipelineContext<*, ApplicationCall>.chottoCall
	get() = call.chottoCall
