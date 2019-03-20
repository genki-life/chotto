package team.genki.chotto.core

import com.github.fluidsonic.fluid.json.*
import team.genki.chotto.core.CommandRequest.*
import kotlin.reflect.KClass


internal class CommandRequestJsonCodec(
	private val metaClass: KClass<out Meta>,
	descriptors: Collection<Command.Typed.Descriptor<*, *>>
) : AbstractJSONCodec<CommandRequest<*, *>, JSONCodingContext>() {

	private val descriptorByName = descriptors.associateBy { it.name }


	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<CommandRequest<*, *>>): CommandRequest<*, *> {
		var descriptor: Command.Typed.Descriptor<*, *>? = null
		var command: Command.Typed<*, *>? = null
		var commandSkipped = false // TODO make order of properties irrelevant
		var meta: Meta? = null

		readMapByElementValue { key ->
			when (key) {
				"meta" -> meta = readValueOfType(jsonCodingType(metaClass))

				"name" -> descriptor = readString().let { name ->
					descriptorByName[name] ?: invalidValueError("unknown command '$name'")
				}

				"command" ->
					descriptor?.commandClass
						?.let { command = readValueOfType(jsonCodingType(it)) }
						?: run {
							commandSkipped = true
							skipValue()
						}

				else -> skipValue()
			}
		}

		descriptor ?: missingPropertyError("name")

		return CommandRequest(
			command = command
				?: if (commandSkipped) invalidPropertyError("name", "property must occur before property 'command'")
				else missingPropertyError("command"),
			meta = meta!! // FIXME optional?
		)
	}


	override fun JSONEncoder<JSONCodingContext>.encode(value: CommandRequest<*, *>) {
		writeIntoMap {
			writeMapElement("name", value.command.descriptor.name) // TODO name must come first for now - lift this limitation if possible
			writeMapElement("meta", value.meta) // FIXME optional?
			writeMapElement("command", value.command)
		}
	}
}
