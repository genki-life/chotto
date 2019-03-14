package team.genki.chotto

import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.ApplicationFeature
import io.ktor.application.call
import io.ktor.util.AttributeKey


internal class ChottoTransactionFeature<Context : ChottoContext, Transaction : ChottoTransaction>(
	private val service: ChottoService<Context, Transaction>,
	private val context: Context
) : ApplicationFeature<ApplicationCallPipeline, Unit, Unit> {

	override val key = AttributeKey<Unit>("Chotto: transaction feature")


	@Suppress("UNCHECKED_CAST")
	override fun install(pipeline: ApplicationCallPipeline, configure: Unit.() -> Unit) {
		Unit.configure()

		pipeline.intercept(ApplicationCallPipeline.Setup) {
			call.attributes.put(transactionAttributeKey, service.createTransaction(context = this@ChottoTransactionFeature.context, call = call))
		}
	}


	companion object {

		val transactionAttributeKey = AttributeKey<ChottoTransaction>("Chotto: transaction")
	}
}
