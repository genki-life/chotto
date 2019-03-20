package team.genki.chotto.server

import com.github.fluidsonic.fluid.json.*
import io.ktor.application.ApplicationCall
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.ApplicationFeature
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.request.ApplicationReceivePipeline
import io.ktor.request.ApplicationReceiveRequest
import io.ktor.request.contentCharset
import io.ktor.request.contentType
import io.ktor.util.AttributeKey
import io.ktor.util.cio.toByteArray
import io.ktor.util.pipeline.PipelineContext
import team.genki.chotto.core.*


internal object CommandRequestFeature : ApplicationFeature<ApplicationCallPipeline, Unit, Unit> {

	override val key = AttributeKey<Unit>("Chotto: command request feature")


	@Suppress("UNCHECKED_CAST")
	override fun install(pipeline: ApplicationCallPipeline, configure: Unit.() -> Unit) {
		Unit.configure()

		pipeline.receivePipeline.intercept(ApplicationReceivePipeline.Transform) { subject ->
			val data = subject.value as? CommandRequestPipelineData
				?: error("unexpected value in receive pipeline: ${subject.value}")

			proceedWith(ApplicationReceiveRequest(
				type = subject.type,
				value = parseRequest(
					body = receiveBody(),
					jsonParser = data.model.jsonConverter.parser
				)
			))
		}
	}


	private fun parseRequest(body: String, jsonParser: JSONCodingParser) =
		try {
			jsonParser.parseValueOfType<CommandRequest<*, *>>(body)
		}
		catch (e: JSONException) {
			if (e is JSONException.Schema || e is JSONException.Syntax) {
				throw CommandFailure(
					code = "invalidRequest",
					developerMessage = e.message,
					userMessage = CommandFailure.genericUserMessage,
					cause = e
				)
			}

			throw e
		}


	private suspend fun PipelineContext<ApplicationReceiveRequest, ApplicationCall>.receiveBody(): String {
		val request = call.request
		if (!request.contentType().withoutParameters().match(ContentType.Application.Json))
			throw CommandFailure(
				code = "invalidRequest",
				developerMessage = "Expected content of type '${ContentType.Application.Json}'",
				userMessage = CommandFailure.genericUserMessage
			)

		return request.receiveChannel().toByteArray().toString(request.contentCharset() ?: Charsets.UTF_8)
	}
}
