package team.genki.chotto.core

import kotlinx.serialization.*
import kotlinx.serialization.internal.*


internal class ConcreteCommandRequestSerializer<TMeta : CommandRequestMeta>(
	commandDescriptors: Set<Command.Typed.Descriptor<*, *>>,
	private val metaSerializer: KSerializer<TMeta>
) : KSerializer<CommandRequest<*, TMeta>> {

	private val commandDescriptorByName = commandDescriptors.associateBy { it.name }


	override val descriptor = object : SerialClassDescImpl("team.genki.chotto.core.CommandRequest") {
		init {
			addElement("meta")
			addElement("commandName")
			addElement("command")
		}
	}


	@UseExperimental(ImplicitReflectionSerializer::class)
	override fun deserialize(decoder: Decoder): CommandRequest<*, TMeta> {
		var commandDescriptor: Command.Typed.Descriptor<*, *>? = null
		lateinit var meta: TMeta
		lateinit var command: Command.Typed<*, *>

		@Suppress("NAME_SHADOWING")
		val decoder = decoder.beginStructure(CommandFailureSerializer.descriptor)

		loop@ while (true) {
			when (val index = decoder.decodeElementIndex(descriptor)) {
				CompositeDecoder.READ_DONE -> break@loop
				0 -> meta = decoder.decodeSerializableElement(descriptor, index, metaSerializer)
				1 -> {
					val name = decoder.decodeStringElement(descriptor, index)
					commandDescriptor = commandDescriptorByName[name] ?: throw SerializationException("Unknown command: $name")
				}
				2 -> {
					commandDescriptor ?: throw SerializationException("'commandName' must come before 'command'")
					command = decoder.decodeSerializableElement(descriptor, index, commandDescriptor.commandClass.serializer())
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
	@UseExperimental(ImplicitReflectionSerializer::class)
	override fun serialize(encoder: Encoder, obj: CommandRequest<*, TMeta>) {
		encoder.beginStructure(descriptor).apply {
			encodeSerializableElement(descriptor, 0, metaSerializer, obj.meta)
			encodeStringElement(descriptor, 1, obj.command.descriptor.name)
			encodeSerializableElement(descriptor, 2, obj.command::class.serializer() as KSerializer<Command>, obj.command)
			endStructure(descriptor)
		}
	}
}
