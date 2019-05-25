package team.genki.chotto.core

import kotlinx.serialization.*


@Serializable(with = RefreshTokenSerializer::class)
data /*inline*/ class RefreshToken(val value: String) {

	override fun toString() = "<redacted>"


	companion object
}


@Serializer(forClass = RefreshToken::class)
internal object RefreshTokenSerializer : KSerializer<RefreshToken> {

	override fun deserialize(decoder: Decoder) =
		RefreshToken(decoder.decodeString())


	override fun serialize(encoder: Encoder, obj: RefreshToken) {
		encoder.encodeString(obj.value)
	}
}
