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

		val _userMessage = userMessage ?: missingPropertyError("userMessage")

		return CommandFailure(
			code = code ?: missingPropertyError("code"),
			developerMessage = developerMessage ?: _userMessage,
			userMessage = _userMessage
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
