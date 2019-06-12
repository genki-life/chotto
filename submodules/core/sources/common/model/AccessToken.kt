package team.genki.chotto.core

import kotlinx.serialization.*


@Serializable(with = AccessTokenSerializer::class)
data /*inline*/ class AccessToken(val value: String) {

	init {
		freeze()
	}


	override fun toString() = "<redacted>"


	companion object
}


@Serializer(forClass = AccessToken::class)
internal object AccessTokenSerializer : KSerializer<AccessToken> {

	override fun deserialize(decoder: Decoder) =
		AccessToken(decoder.decodeString())


	override fun serialize(encoder: Encoder, obj: AccessToken) {
		encoder.encodeString(obj.value)
	}
}
