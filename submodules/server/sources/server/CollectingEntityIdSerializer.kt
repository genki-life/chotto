package team.genki.chotto.server

import kotlinx.coroutines.channels.*
import kotlinx.serialization.*
import kotlinx.serialization.modules.*
import org.slf4j.*
import team.genki.chotto.core.*


internal class CollectingEntityIdSerializer<Transaction : ChottoTransaction>(
	context: SerialModule,
	private val resolver: EntityResolver<Transaction>,
	private val transaction: Transaction
) : KSerializer<EntityId> {

	private val base = context.getContextual(EntityId::class)!!
	private var pendingIds = hashSetOf<EntityId>()
	private val processedIds = hashSetOf<EntityId>()
	private val unresolvableIds = hashSetOf<EntityId>()


	override val descriptor get() = base.descriptor


	override fun deserialize(decoder: Decoder) =
		base.deserialize(decoder)


	override fun serialize(encoder: Encoder, obj: EntityId) {
		if (!processedIds.contains(obj))
			pendingIds.add(obj)

		base.serialize(encoder, obj)
	}


	suspend fun writeTo(encoder: Encoder) {
		val idSerializer = base
		val dummyEntitySerializer = PolymorphicSerializer(Entity::class)
		val descriptor = (idSerializer to dummyEntitySerializer).map.descriptor
		var index = 0

		encoder.beginCollection(descriptor, 0, idSerializer, dummyEntitySerializer).apply {
			var runCount = 0

			while (pendingIds.isNotEmpty()) {
				runCount += 1

				val ids = pendingIds
				processedIds += ids
				pendingIds = hashSetOf()

				val entities = resolve(ids)
				for (entity in entities) {
					encodeSerializableElement(descriptor, index++, idSerializer, entity.id)

					@Suppress("UNCHECKED_CAST")
					val valueSerializer = entity.id.type.entitySerializer as KSerializer<Entity>

					encodeSerializableElement(descriptor, index++, valueSerializer, entity)
				}
			}

			if (unresolvableIds.isNotEmpty()) {
				log.warn("Response references entities which cannot be found: " + unresolvableIds.joinToString(", "))
			}

			if (runCount >= 4) {
				log.debug("Response serialization took $runCount runs! Consider flattening the entity hierarchy.")
			}

			endStructure(descriptor)
		}
	}


	private suspend fun resolve(ids: Set<EntityId>): Collection<Entity> {
		val entitiesById: MutableMap<EntityId, Entity> = resolver
			.resolve(ids = ids)
			.map { it.toClientModel(transaction) }
			.associateByTo(hashMapOf(), Entity::id)

		for (id in ids)
			if (!entitiesById.containsKey(id))
				unresolvableIds.add(id)

		return entitiesById.values
	}


	companion object {

		private val log = LoggerFactory.getLogger(CollectingEntityIdSerializer::class.java)!!
	}
}
