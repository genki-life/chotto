package team.genki.chotto

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.util.pipeline.PipelineContext


abstract class ChottoModule<Context : ChottoContext, Transaction : ChottoTransaction> {

	internal fun configure() =
		ChottoModuleConfiguration(this).apply { configure() }


	abstract fun ChottoModuleConfiguration<Context, Transaction>.configure()


	@Suppress("UNCHECKED_CAST")
	val ApplicationCall.transaction
		get() = attributes[ChottoTransactionFeature.transactionAttributeKey] as Transaction


	val PipelineContext<*, ApplicationCall>.transaction
		get() = call.transaction
}


val ApplicationCall.transaction
	get() = attributes[ChottoTransactionFeature.transactionAttributeKey]


val PipelineContext<*, ApplicationCall>.transaction
	get() = call.transaction
