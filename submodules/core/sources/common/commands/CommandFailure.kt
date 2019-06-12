package team.genki.chotto.core

import kotlinx.serialization.*
import kotlinx.serialization.internal.*


@Serializable(with = CommandFailureSerializer::class)
class CommandFailure(
	val code: String,
	val userMessage: String,
	val developerMessage: String = userMessage,
	cause: Throwable? = null
) : Exception(developerMessage, cause) {

	override val message
		get() = "[$code] $developerMessage"


	companion object {

		const val genericUserMessage = "Looks like we're having some trouble right now.\nPlease try again soon."
	}
}


internal fun CommandFailure.Companion.invalidResponse(response: String, cause: Throwable? = null) =
	CommandFailure(
		code = "invalid response",
		userMessage = genericUserMessage,
		developerMessage = "Server returned an invalid response: $response",
		cause = cause
	)


@Serializer(forClass = CommandFailure::class)
internal object CommandFailureSerializer : KSerializer<CommandFailure> {

	override val descriptor = object : SerialClassDescImpl("team.genki.chotto.core.CommandFailure") {
		init {
			addElement("code")
			addElement("developerMessage", isOptional = true)
			addElement("userMessage")
		}
	}


	override fun deserialize(decoder: Decoder): CommandFailure {
		decoder.beginStructure(descriptor).apply {
			lateinit var code: String
			var developerMessage: String? = null
			lateinit var userMessage: String

			loop@ while (true) {
				when (val index = decodeElementIndex(descriptor)) {
					CompositeDecoder.READ_DONE -> break@loop
					0 -> code = decodeStringElement(descriptor, index)
					1 -> developerMessage = decodeStringElement(descriptor, index)
					2 -> userMessage = decodeStringElement(descriptor, index)
					else -> throw SerializationException("Unknown index $index")
				}
			}

			endStructure(descriptor)

			return CommandFailure(
				code = code,
				developerMessage = developerMessage ?: userMessage,
				userMessage = userMessage
			)
		}
	}


	override fun serialize(encoder: Encoder, obj: CommandFailure) {
		encoder.beginStructure(descriptor).apply {
			encodeStringElement(descriptor, 0, obj.code)
			if (obj.developerMessage != obj.userMessage)
				encodeStringElement(descriptor, 1, obj.developerMessage)
			encodeStringElement(descriptor, 2, obj.userMessage)
			endStructure(descriptor)
		}
	}
}
