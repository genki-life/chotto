package team.genki.chotto.core

import kotlinx.serialization.*
import kotlinx.serialization.internal.*


@Serializable(with = ChangeSerializer::class)
data /*inline*/ class Change<out Value>(val value: Value) {

	init {
		freeze()
	}


	companion object
}


@Serializer(forClass = Change::class)
internal class ChangeSerializer<Value>(
	private val valueSerializer: KSerializer<Value>
) : KSerializer<Change<Value>> {

	override val descriptor = object : SerialClassDescImpl("Change") {}


	override fun deserialize(decoder: Decoder) =
		Change(decoder.decodeSerializableValue(valueSerializer))


	override fun serialize(encoder: Encoder, obj: Change<Value>) {
		encoder.encodeSerializableValue(valueSerializer, obj.value)
	}
}
