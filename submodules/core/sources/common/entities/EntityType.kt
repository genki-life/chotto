package team.genki.chotto.core

import kotlinx.serialization.*
import kotlin.reflect.*


interface EntityType<TId : EntityId, TEntity : Entity> {

	val entitySerializer: KSerializer<TEntity>
	val idClass: KClass<TId>
	val namespace: String

	fun parseId(string: String): TId?
	fun serializeId(id: TId): String

	fun TId.serialize(): String =
		serializeId(this)


	abstract class Typed<TId : EntityId.Typed<TId, TEntity>, TEntity : Entity.Typed<TId, TEntity>>(
		final override val namespace: String,
		final override val entitySerializer: KSerializer<TEntity>,
		final override val idClass: KClass<TId>,
		private val idFactory: (value: String) -> TId
	) : EntityType<TId, TEntity> {

		internal val prefix = "$namespace/"

		final override fun equals(other: Any?) =
			this === other

		final override fun hashCode() =
			super.hashCode()

		final override fun parseId(string: String) =
			string
				.takeIf { it.startsWith(prefix) || !it.contains('/') }
				?.removePrefix(prefix)
				?.let(idFactory)


		final override fun serializeId(id: TId) =
			prefix + id.value


		final override fun toString() =
			namespace

		final override fun TId.serialize(): String =
			serializeId(this)
	}


	companion object
}
