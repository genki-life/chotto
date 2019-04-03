package team.genki.chotto.core

import com.github.fluidsonic.fluid.json.*


internal object CommandFailureJsonCodec : AbstractJSONCodec<CommandFailure, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<CommandFailure>): CommandFailure {
		var code: String? = null
		var developerMessage: String? = null
		var userMessage: String? = null

		readFromMapByElementValue { key ->
			when (key) {
				"code" -> code = readString()
				"developerMessage" -> developerMessage = readString()
				"userMessage" -> userMessage = readString()
				else -> skipValue()
			}
		}

		// https://youtrack.jetbrains.com/issue/KT-29510 (thus fluid-json cannot use contracts to support smart-casts here)
		val nonNullUserMessage = userMessage ?: missingPropertyError("userMessage")

		return CommandFailure(
			code = code ?: missingPropertyError("code"),
			developerMessage = developerMessage ?: nonNullUserMessage,
			userMessage = nonNullUserMessage
		)
	}


	override fun JSONEncoder<JSONCodingContext>.encode(value: CommandFailure) {
		writeIntoMap {
			writeMapElement("code", value.code)
			writeMapElement("developerMessage", value.developerMessage)
			writeMapElement("userMessage", value.userMessage)
		}
	}
}
