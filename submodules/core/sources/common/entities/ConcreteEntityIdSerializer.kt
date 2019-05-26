package team.genki.chotto.core

import kotlinx.serialization.*
import kotlinx.serialization.internal.*


internal class ConcreteEntityIdSerializer(
	types: Set<EntityType<*, *>>
) : KSerializer<EntityId> {

	private val typeByNamespace = types.associateBy { it.namespace }


	override val descriptor = StringDescriptor.withName("team.genki.chotto.core.EntityId")


	override fun deserialize(decoder: Decoder) =
		decoder.decodeString().let { string ->
			val namespace = string.substringBeforeLast('/')
			typeByNamespace[namespace]?.parseId(string) ?: throw SerializationException("'$string' is not a valid ID")
		}


	override fun serialize(encoder: Encoder, obj: EntityId) {
		@Suppress("UNCHECKED_CAST")
		val type = obj.type as EntityType<EntityId, *>

		encoder.encodeString(type.serializeId(obj))
	}
}
