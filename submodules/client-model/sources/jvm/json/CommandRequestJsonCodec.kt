package team.genki.chotto.client.model

import team.genki.chotto.client.model.CommandRequest.*
import com.github.fluidsonic.fluid.json.*
import kotlin.reflect.KClass


internal class CommandRequestJsonCodec(
	private val metaClass: KClass<out CommandRequest.Meta>,
	private val commandDescriptorByName: Map<String, Command.Descriptor<*, *>>
) : AbstractJSONCodec<CommandRequest<*, *>, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<CommandRequest<*, *>>): CommandRequest<*, *> {
		var descriptor: Command.Descriptor<*, *>? = null
		var command: Command<*>? = null
		var commandSkipped = false // TODO make order of properties irrelevant
		var meta: Meta? = null

		readMapByElementValue { key ->
			when (key) {
				"meta" -> meta = readValueOfType(jsonCodingType(metaClass))

				"name" -> descriptor = readString().let { name ->
					commandDescriptorByName[name] ?: invalidValueError("unknown command '$name'")
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
