package team.genki.chotto.core

import kotlinx.serialization.*


@Serializable(with = EmailAddressSerializer::class)
data /*inline*/ class EmailAddress(val value: String) {

	fun toLowerCase() =
		EmailAddress(value.toLowerCase())


	override fun toString() = value


	companion object
}


@Serializer(forClass = AccessToken::class)
internal object EmailAddressSerializer : KSerializer<EmailAddress> {

	override fun deserialize(decoder: Decoder) =
		EmailAddress(decoder.decodeString())


	override fun serialize(encoder: Encoder, obj: EmailAddress) {
		encoder.encodeString(obj.value)
	}
}

