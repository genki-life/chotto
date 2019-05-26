package team.genki.chotto.server

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.util.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.json.internal.*
import kotlinx.serialization.modules.*
import team.genki.chotto.core.*


internal object CommandResponseFeature : ApplicationFeature<ApplicationCallPipeline, Unit, Unit> {

	override val key = AttributeKey<Unit>("Chotto: command response feature")


	@Suppress("UNCHECKED_CAST")
	override fun install(pipeline: ApplicationCallPipeline, configure: Unit.() -> Unit) {
		Unit.configure()

		pipeline.sendPipeline.intercept(ApplicationSendPipeline.Render) { subject ->
			if (subject is OutgoingContent) return@intercept

			val data = subject as? CommandResponsePipelineData
				?: error("unexpected value in send pipeline: $subject")

			val call = chottoCall

			proceedWith(serializeResponse(
				data = data,
				entityResolver = call.entityResolver as EntityResolver<ChottoTransaction>,
				transaction = call.transactionController.transaction
			))
		}
	}


	@Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER", "CANNOT_OVERRIDE_INVISIBLE_MEMBER")
	@UseExperimental(ImplicitReflectionSerializer::class)
	private suspend fun serializeResponse(
		data: CommandResponsePipelineData,
		entityResolver: EntityResolver<ChottoTransaction>,
		transaction: ChottoTransaction
	): OutgoingContent {
		// we can't use KSerializer directly due to on-the-fly entity resolution

		val resultSerializer = data.result::class.serializer() as KSerializer<Any>
		val metaSerializer = data.model.commandResponseMetaClass.serializer() as KSerializer<CommandResponseMeta>
		val responseSerializer = CommandResponse.serializer(resultSerializer, metaSerializer)
		val collectingEntityIdSerializer = CollectingEntityIdSerializer(
			context = data.model.serializationContext,
			resolver = entityResolver,
			transaction = transaction
		)

		val json = Json(
			configuration = JsonConfiguration.Stable,
			context = data.model.serializationContext overwriteWith serializersModuleOf(EntityId::class, collectingEntityIdSerializer)
		)

		val output = buildString {
			StreamingJsonOutput(
				output = this,
				json = json,
				mode = WriteMode.OBJ,
				modeReuseCache = arrayOfNulls(WriteMode.values().size)
			).let { encoder ->
				val descriptor = CommandRequestStatus.serializer().descriptor
				(encoder.beginStructure(descriptor, resultSerializer, metaSerializer) as ElementValueEncoder).let { encoder ->
					encoder.encodeStringElement(descriptor, 0, "success")
					encoder.encodeElement(descriptor, 2)

					val descriptor = responseSerializer.descriptor
					(encoder.beginStructure(descriptor, resultSerializer, metaSerializer) as ElementValueEncoder).let { encoder ->
						encoder.encodeSerializableElement(descriptor, 1, metaSerializer, data.meta)
						encoder.encodeSerializableElement(descriptor, 2, resultSerializer, data.result)
						encoder.encodeElement(descriptor, 0)
						collectingEntityIdSerializer.writeTo(encoder)

						encoder.endStructure(descriptor)
					}
					encoder.endStructure(descriptor)
				}
			}
		}

		return TextContent(
			text = output,
			contentType = ContentType.Application.Json.withCharset(Charsets.UTF_8)
		)
	}
}
