package team.genki.chotto.core

import kotlinx.serialization.*
import kotlinx.serialization.internal.*


@Serializable(with = CommandRequestSerializer::class)
data class CommandRequest<out TCommand : TypedCommand<*, *>, out TMeta : CommandRequestMeta>(
	val command: TCommand,
	val meta: TMeta
) {

	companion object {

		fun serializer(): KSerializer<CommandRequest<*, *>> =
			CommandRequestSerializer()
	}
}


interface CommandRequestMeta


@Serializable(with = CommandRequestStatusSerializer::class)
sealed class CommandRequestStatus {

	@Serializable(with = CommandRequestStatusSerializer::class)
	data class Failure(val cause: CommandFailure) : CommandRequestStatus() {

		companion object
	}


	data class Success<TResult : Any, TMeta : CommandResponseMeta>(val response: CommandResponse<TResult, TMeta>) : CommandRequestStatus() {

		companion object
	}


	companion object {

		fun <TResult : Any, TMeta : CommandResponseMeta> serializer(resultSerializer: KSerializer<TResult>, metaSerializer: KSerializer<TMeta>): KSerializer<CommandRequestStatus> =
			CommandRequestStatusSerializer(resultSerializer, metaSerializer)
	}
}


@Serializer(forClass = CommandRequest::class)
internal class CommandRequestSerializer<TCommand : TypedCommand<*, *>, TMeta : CommandRequestMeta> : KSerializer<CommandRequest<TCommand, TMeta>> {

	override val descriptor = StringDescriptor.withName("team.genki.chotto.core.EntityId")


	override fun serialize(encoder: Encoder, obj: CommandRequest<TCommand, TMeta>) {
		val serializer = encoder.context.getContextual(CommandRequest::class)
			?: error("A serializer for team.genki.chotto.core.EntityId must be specified in the SerialModule")

		encoder.encodeSerializableValue(serializer, obj)
	}


	override fun deserialize(decoder: Decoder): CommandRequest<TCommand, TMeta> {
		val serializer = decoder.context.getContextual(CommandRequest::class)
			?: error("A serializer for team.genki.chotto.core.EntityId must be specified in the SerialModule")

		@Suppress("UNCHECKED_CAST")
		return decoder.decodeSerializableValue(serializer as KSerializer<CommandRequest<TCommand, TMeta>>)
	}
}


@Serializer(forClass = CommandRequestStatus::class)
private class CommandRequestStatusSerializer<TResult : Any, TMeta : CommandResponseMeta>(
	val resultSerializer: KSerializer<TResult>? = null,
	val metaSerializer: KSerializer<TMeta>? = null
) : KSerializer<CommandRequestStatus> {

	override val descriptor = object : SerialClassDescImpl("team.genki.chotto.core.CommandRequest.Status") {
		init {
			addElement("status")
			addElement("cause", isOptional = true)
			addElement("response", isOptional = true)
		}
	}


	override fun deserialize(decoder: Decoder): CommandRequestStatus {
		decoder.beginStructure(descriptor).apply {
			lateinit var status: String
			var cause: CommandFailure? = null
			var response: CommandResponse<TResult, TMeta>? = null

			loop@ while (true) {
				when (val index = decodeElementIndex(descriptor)) {
					CompositeDecoder.READ_DONE -> break@loop
					0 -> status = decodeStringElement(descriptor, index)
					1 -> cause = decodeSerializableElement(descriptor, index, CommandFailure.serializer())
					2 -> {
						resultSerializer ?: error("cannot use CommandRequestStatusSerializer.Failure.serializer() for decoding Success")
						metaSerializer ?: error("cannot use CommandRequestStatusSerializer.Failure.serializer() for decoding Success")

						response = decodeSerializableElement(descriptor, index, CommandResponse.serializer(resultSerializer, metaSerializer))
					}
					else -> throw SerializationException("Unknown index $index")
				}
			}

			endStructure(descriptor)

			return when (status) {
				"failure" -> CommandRequestStatus.Failure(
					cause = cause ?: throw SerializationException("Missing 'cause'")
				)
				"success" -> CommandRequestStatus.Success(
					response = response ?: throw SerializationException("Missing 'response'")
				)
				else -> throw SerializationException("Unknown status: $status")
			}
		}
	}


	@Suppress("UNCHECKED_CAST")
	override fun serialize(encoder: Encoder, obj: CommandRequestStatus) {
		encoder.beginStructure(descriptor).apply {
			when (obj) {
				is CommandRequestStatus.Failure -> {
					encodeStringElement(descriptor, 0, "failure")
					encodeSerializableElement(descriptor, 1, CommandFailure.serializer(), obj.cause)
				}

				// FIXME entities missing here
				is CommandRequestStatus.Success<*, *> -> {
					resultSerializer ?: error("cannot use CommandRequestStatusSerializer.Failure.serializer() for encoding Success")
					metaSerializer ?: error("cannot use CommandRequestStatusSerializer.Failure.serializer() for encoding Success")

					encodeStringElement(descriptor, 0, "success")
					encodeSerializableElement(descriptor, 2, CommandResponse.serializer(resultSerializer, metaSerializer) as KSerializer<Any>, obj.response)
				}
			}

			endStructure(descriptor)
		}
	}
}
