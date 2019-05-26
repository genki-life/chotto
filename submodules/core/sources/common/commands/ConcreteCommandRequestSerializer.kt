package team.genki.chotto.core

import kotlinx.serialization.*
import kotlinx.serialization.internal.*


internal class ConcreteCommandRequestSerializer<TMeta : CommandRequestMeta>(
	commandDefinitions: Set<TypedCommandDefinition<*, *>>,
	private val metaSerializer: KSerializer<TMeta>
) : KSerializer<CommandRequest<*, TMeta>> {

	private val commandDefinitionByName = commandDefinitions.associateBy { it.name }


	override val descriptor = object : SerialClassDescImpl("team.genki.chotto.core.CommandRequest") {
		init {
			addElement("meta")
			addElement("commandName")
			addElement("command")
		}
	}


	override fun deserialize(decoder: Decoder): CommandRequest<*, TMeta> {
		var commandDefinition: TypedCommandDefinition<*, *>? = null
		lateinit var meta: TMeta
		lateinit var command: TypedCommand<*, *>

		@Suppress("NAME_SHADOWING")
		val decoder = decoder.beginStructure(CommandFailureSerializer.descriptor)

		loop@ while (true) {
			when (val index = decoder.decodeElementIndex(descriptor)) {
				CompositeDecoder.READ_DONE -> break@loop
				0 -> meta = decoder.decodeSerializableElement(descriptor, index, metaSerializer)
				1 -> {
					val name = decoder.decodeStringElement(descriptor, index)
					commandDefinition = commandDefinitionByName[name] ?: throw SerializationException("Unknown command: $name")
				}
				2 -> {
					commandDefinition ?: throw SerializationException("'commandName' must come before 'command'")
					command = decoder.decodeSerializableElement(descriptor, index, commandDefinition.serializer)
				}
				else -> throw SerializationException("Unknown index $index")
			}
		}

		decoder.endStructure(CommandFailureSerializer.descriptor)

		return CommandRequest(
			command = command,
			meta = meta
		)
	}


	@Suppress("UNCHECKED_CAST")
	override fun serialize(encoder: Encoder, obj: CommandRequest<*, TMeta>) {
		encoder.beginStructure(descriptor).apply {
			encodeSerializableElement(descriptor, 0, metaSerializer, obj.meta)
			encodeStringElement(descriptor, 1, obj.command.definition.name)
			encodeSerializableElement(descriptor, 2, obj.command.definition.serializer as KSerializer<Command>, obj.command)
			endStructure(descriptor)
		}
	}
}
