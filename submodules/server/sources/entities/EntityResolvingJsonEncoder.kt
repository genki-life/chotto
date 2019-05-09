package team.genki.chotto.server

import com.github.fluidsonic.fluid.json.*
import kotlinx.coroutines.channels.*
import org.slf4j.*
import team.genki.chotto.core.*
import java.io.*


internal class EntityResolvingJsonEncoder<Transaction : ChottoTransaction>(
	private val codecProvider: JSONCodecProvider<JSONCodingContext>,
	private val resolver: EntityResolver<Transaction>,
	private val transaction: Transaction,
	writer: Writer
) : JSONEncoder<JSONCodingContext>, JSONWriter by JSONWriter.build(writer) {

	private val cachedEntities: MutableMap<EntityId, Entity> = hashMapOf()
	private val entityReferences: MutableSet<EntityId> = hashSetOf()


	override val context
		get() = JSONCodingContext.empty


	suspend fun writeEntities() {
		writeMapStart()

		val resolvedIds = hashSetOf<EntityId>()
		val unresolvedIds = mutableListOf<EntityId>()

		var runCount = 0

		var idsToResolve: Set<EntityId> = entityReferences
		while (idsToResolve.isNotEmpty()) {
			runCount += 1
			resolvedIds += idsToResolve

			val resolvedEntitiesById: MutableMap<EntityId, Entity> = resolver
				.resolve(ids = idsToResolve)
				.map { it.run { transaction.toCoreModel() } }
				.associateByTo(hashMapOf(), Entity::id)

			for (id in idsToResolve) {
				if (!resolvedEntitiesById.containsKey(id)) {
					val cachedEntity = cachedEntities[id]
					if (cachedEntity != null)
						resolvedEntitiesById[id] = cachedEntity
					else
						unresolvedIds.add(id)
				}
			}

			for (entity in resolvedEntitiesById.values) {
				writeValue(entity.id, collect = false)
				writeValue(entity)
			}

			idsToResolve = entityReferences - resolvedIds
		}

		if (unresolvedIds.isNotEmpty()) {
			log.warn("Response references entities which cannot be found: " + unresolvedIds.joinToString(", "))
		}

		if (runCount >= 4) {
			log.debug("Response serialization took $runCount runs! Consider flattening the entity hierarchy.")
		}

		writeMapEnd()
	}


	@Suppress("UNCHECKED_CAST")
	private fun writeValue(value: Any, collect: Boolean) {
		withErrorChecking {
			if (collect && value is EntityId) {
				entityReferences += value
			}

			(codecProvider.encoderCodecForClass(value::class) as JSONEncoderCodec<Any, JSONCodingContext>?)
				?.run {
					try {
						isolateValueWrite {
							encode(value = value)
						}
					}
					catch (e: JSONException) {
						// TODO remove .java once KT-28418 is fixed
						e.addSuppressed(JSONException.Serialization("… when encoding value of ${value::class} using ${this::class.java.name}: $value"))
						throw e
					}
				}
				?: throw JSONException.Serialization(
					message = "No encoder codec registered for ${value::class}: $value",
					path = path
				)
		}
	}


	override fun writeValue(value: Any) =
		writeValue(value, collect = true)


	companion object {

		private val log = LoggerFactory.getLogger(EntityResolvingJsonEncoder::class.java)!!
	}
}
