package team.genki.chotto.core

import com.github.fluidsonic.fluid.json.*
import team.genki.chotto.core.CommandRequest.*
import kotlin.reflect.KClass


internal class CommandRequestJsonCodec(
	private val metaClass: KClass<out Meta>,
	descriptors: Collection<Command.Typed.Descriptor<*, *>>
) : AbstractJSONCodec<CommandRequest<*, *>, JSONCodingContext>(
	additionalProviders = listOf(StatusCodec)
) {

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


	private object StatusCodec : AbstractJSONCodec<Status<*, *>, JSONCodingContext>() {

		override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Status<*, *>>): Status<*, *> {
			var denominator: String? = null
			var status: Status<*, *>? = null

			readMapByElementValue { key ->
				when (key) {
					"status" -> {
						if (denominator != null) schemaError("'status' cannot occur multiple times")
						denominator = readString()
					}

					"cause" -> when (denominator) {
						"failure" -> {
							if (status != null) schemaError("'cause' cannot occur multiple times")
							status = Status.Failure<Any, CommandResponse.Meta>(readValueOfType(jsonCodingType(CommandFailure::class)))
						}
						else -> skipValue()
					}

					"response" -> when (denominator) {
						"success" -> {
							if (status != null) schemaError("'success' cannot occur multiple times")
							status = Status.Success(readValueOfType(jsonCodingType(
								CommandResponse::class,
								valueType.arguments[0].rawClass, // TODO doesn't look right
								valueType.arguments[1].rawClass // TODO doesn't look right
							)))
						}
						else -> skipValue()
					}

					else -> skipValue()
				}
			}

			denominator ?: missingPropertyError("status")

			return status ?: when (denominator) {
				"failure" -> missingPropertyError("cause")
				"success" -> missingPropertyError("response")
				else -> invalidPropertyError("status", "must be either 'success' or 'failure' but is '$denominator'")
			}
		}


		override fun JSONEncoder<JSONCodingContext>.encode(value: Status<*, *>) {
			writeIntoMap {
				when (value) {
					is Status.Failure -> {
						writeMapElement("status", string = "failure")
						writeMapElement("cause", value = value.cause)
					}
					is Status.Success -> {
						writeMapElement("status", string = "success")
						writeMapElement("response", value = value.response)
					}
				}
			}
		}
	}
}
