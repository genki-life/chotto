package team.genki.chotto.core

import kotlinx.serialization.*


@Serializable(with = PhoneNumberSerializer::class)
data /*inline*/ class PhoneNumber(val value: String) {

	override fun toString() = value


	companion object
}


@Serializer(forClass = AccessToken::class)
internal object PhoneNumberSerializer : KSerializer<PhoneNumber> {

	override fun deserialize(decoder: Decoder) =
		PhoneNumber(decoder.decodeString())


	override fun serialize(encoder: Encoder, obj: PhoneNumber) {
		encoder.encodeString(obj.value)
	}
}
