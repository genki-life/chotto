package team.genki.chotto.core

import kotlinx.serialization.*


@Serializable(with = PasswordSerializer::class)
data /*inline*/ class Password(val value: String) {

	init {
		freeze()
	}


	override fun toString() = "<redacted>"


	companion object
}


@Serializer(forClass = Password::class)
internal object PasswordSerializer : KSerializer<Password> {

	override fun deserialize(decoder: Decoder) =
		Password(decoder.decodeString())


	override fun serialize(encoder: Encoder, obj: Password) {
		encoder.encodeString(obj.value)
	}
}
