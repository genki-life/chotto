package team.genki.chotto.core

import kotlinx.serialization.*
import kotlinx.serialization.internal.*


@Serializable(with = CommandRequestSerializer::class)
data class CommandRequest<out TCommand : TypedCommand<out TCommand, *>, out TMeta : CommandRequestMeta>(
	val command: TCommand,
	val meta: TMeta
) {

	companion object {

		fun serializer(): KSerializer<CommandRequest<*, *>> =
			CommandRequestSerializer
	}
}


interface CommandRequestMeta


@Serializable(with = CommandRequestStatusSerializer::class)
sealed class CommandRequestStatus<out TResult : Any, out TMeta : CommandResponseMeta> {

	data class Failure<out TResult : Any, out TMeta : CommandResponseMeta>(val cause: CommandFailure) : CommandRequestStatus<TResult, TMeta>() {

		companion object {

			fun serializer(): KSerializer<Failure<*, *>> =
				CommandRequestStatusFailureSerializer
		}
	}


	data class Success<out TResult : Any, out TMeta : CommandResponseMeta>(val response: CommandResponse<TResult, TMeta>) : CommandRequestStatus<TResult, TMeta>() {

		companion object
	}


	companion object
}


@Serializer(forClass = CommandRequest::class)
internal object CommandRequestSerializer : KSerializer<CommandRequest<*, *>> {

	override val descriptor = StringDescriptor.withName("team.genki.chotto.core.EntityId")


	override fun serialize(encoder: Encoder, obj: CommandRequest<*, *>) {
		val serializer = encoder.context.getContextual(CommandRequest::class)
			?: error("A serializer for team.genki.chotto.core.EntityId must be specified in the SerialModule")

		encoder.encodeSerializableValue(serializer, obj)
	}


	override fun deserialize(decoder: Decoder): CommandRequest<*, *> {
		val serializer = decoder.context.getContextual(CommandRequest::class)
			?: error("A serializer for team.genki.chotto.core.EntityId must be specified in the SerialModule")

		@Suppress("UNCHECKED_CAST")
		return decoder.decodeSerializableValue(serializer)
	}
}


@Serializer(forClass = CommandRequestStatus::class)
internal class CommandRequestStatusSerializer<TResult : Any, TMeta : CommandResponseMeta>(
	val resultSerializer: KSerializer<TResult>,
	val metaSerializer: KSerializer<TMeta>
) : KSerializer<CommandRequestStatus<TResult, TMeta>> {

	override val descriptor = object : SerialClassDescImpl("team.genki.chotto.core.CommandRequestStatus") {
		init {
			addElement("status")
			addElement("cause", isOptional = true)
			addElement("response", isOptional = true)
		}
	}


	override fun deserialize(decoder: Decoder): CommandRequestStatus<TResult, TMeta> {
		decoder.beginStructure(descriptor).apply {
			lateinit var status: String
			var cause: CommandFailure? = null
			var response: CommandResponse<TResult, TMeta>? = null

			loop@ while (true) {
				when (val index = decodeElementIndex(descriptor)) {
					CompositeDecoder.READ_DONE -> break@loop
					0 -> status = decodeStringElement(descriptor, index)
					1 -> cause = decodeSerializableElement(descriptor, index, CommandFailure.serializer())
					2 -> response = decodeSerializableElement(descriptor, index, CommandResponse.serializer(resultSerializer, metaSerializer))
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
	override fun serialize(encoder: Encoder, obj: CommandRequestStatus<TResult, TMeta>) {
		encoder.beginStructure(descriptor).apply {
			when (obj) {
				is CommandRequestStatus.Failure -> {
					encodeStringElement(descriptor, 0, "failure")
					encodeSerializableElement(descriptor, 1, CommandFailure.serializer(), obj.cause)
				}

				// FIXME entities missing here
				is CommandRequestStatus.Success<*, *> -> {
					encodeStringElement(descriptor, 0, "success")
					encodeSerializableElement(descriptor, 2, CommandResponse.serializer(resultSerializer, metaSerializer) as KSerializer<Any>, obj.response)
				}
			}

			endStructure(descriptor)
		}
	}
}


@Serializer(forClass = CommandRequestStatus.Failure::class)
internal object CommandRequestStatusFailureSerializer : KSerializer<CommandRequestStatus.Failure<*, *>> {

	override val descriptor = object : SerialClassDescImpl("team.genki.chotto.core.CommandRequestStatus.Failure") {
		init {
			addElement("status")
			addElement("cause")
		}
	}


	override fun deserialize(decoder: Decoder): CommandRequestStatus.Failure<*, *> {
		decoder.beginStructure(descriptor).apply {
			lateinit var status: String
			lateinit var cause: CommandFailure

			loop@ while (true) {
				when (val index = decodeElementIndex(descriptor)) {
					CompositeDecoder.READ_DONE -> break@loop
					0 -> status = decodeStringElement(descriptor, index)
					1 -> cause = decodeSerializableElement(descriptor, index, CommandFailure.serializer())
					else -> throw SerializationException("Unknown index $index")
				}
			}

			endStructure(descriptor)

			return when (status) {
				"failure" -> CommandRequestStatus.Failure<Any, CommandResponseMeta>(cause = cause)
				else -> throw SerializationException("Unexpected status '$status', expected 'failure'")
			}
		}
	}


	@Suppress("UNCHECKED_CAST")
	override fun serialize(encoder: Encoder, obj: CommandRequestStatus.Failure<*, *>) {
		encoder.beginStructure(descriptor).apply {
			encodeStringElement(descriptor, 0, "failure")
			encodeSerializableElement(descriptor, 1, CommandFailure.serializer(), obj.cause)

			endStructure(descriptor)
		}
	}
}
