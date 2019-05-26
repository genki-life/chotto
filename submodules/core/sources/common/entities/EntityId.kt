package team.genki.chotto.core

import kotlinx.serialization.*
import kotlinx.serialization.internal.*


@Serializable(with = EntityIdSerializer::class)
interface EntityId {

	val type: EntityType<*, *>
	val value: String


	companion object {

		fun serializer(): KSerializer<EntityId> =
			EntityIdSerializer
	}


	interface Typed<TId : Typed<TId, TEntity>, TEntity : Entity.Typed<TId, TEntity>> : EntityId {

		override val type: EntityType.Typed<TId, TEntity>
	}
}


@Serializer(forClass = EntityId::class)
object EntityIdSerializer : KSerializer<EntityId> {

	override val descriptor = StringDescriptor.withName("team.genki.chotto.core.EntityId")


	override fun serialize(encoder: Encoder, obj: EntityId) {
		val serializer = encoder.context.getContextual(EntityId::class)
			?: error("A serializer for team.genki.chotto.core.EntityId must be specified in the SerialModule")

		encoder.encodeSerializableValue(serializer, obj)
	}


	override fun deserialize(decoder: Decoder): EntityId {
		val serializer = decoder.context.getContextual(EntityId::class)
			?: error("A serializer for team.genki.chotto.core.EntityId must be specified in the SerialModule")

		return decoder.decodeSerializableValue(serializer)
	}
}
