package team.genki.chotto.core

import kotlin.reflect.*


interface EntityType<TId : EntityId, TEntity : Entity> {

	val entityClass: KClass<TEntity>
	val idClass: KClass<TId>
	val namespace: String

	fun parseId(string: String): TId?
	fun serializeId(id: TId): String

	fun TId.serialize(): String =
		serializeId(this)


	interface Typed<TId : EntityId.Typed<TId, TEntity>, TEntity : Entity.Typed<TId, TEntity>> : EntityType<TId, TEntity> {

		override val entityClass: KClass<TEntity>
		override val idClass: KClass<TId>

		override fun parseId(string: String): TId?
		override fun serializeId(id: TId): String

		override fun TId.serialize(): String =
			serializeId(this)
	}


	companion object {

		@PublishedApi
		internal fun <TId : EntityId.Typed<TId, TEntity>, TEntity : Entity.Typed<TId, TEntity>> create(
			entityClass: KClass<TEntity>,
			namespace: String,
			idClass: KClass<TId>,
			idFactory: (value: String) -> TId
		): Typed<TId, TEntity> =
			ActualEntityType(
				entityClass = entityClass,
				idClass = idClass,
				idFactory = idFactory,
				namespace = namespace
			)
	}
}


inline infix fun <reified TId : EntityId.Typed<TId, TEntity>, reified TEntity : Entity.Typed<TId, TEntity>> String.using(
	noinline idFactory: (value: String) -> TId
): EntityType.Typed<TId, TEntity> =
	EntityType.create(
		entityClass = TEntity::class,
		idClass = TId::class,
		idFactory = idFactory,
		namespace = this
	)


private class ActualEntityType<TId : EntityId.Typed<TId, TEntity>, TEntity : Entity.Typed<TId, TEntity>>(
	override val entityClass: KClass<TEntity>,
	override val idClass: KClass<TId>,
	private val idFactory: (value: String) -> TId,
	override val namespace: String
) : EntityType.Typed<TId, TEntity> {

	internal val prefix = "$namespace/"


	override fun parseId(string: String) =
		string
			.takeIf { it.startsWith(prefix) || !it.contains('/') }
			?.removePrefix(prefix)
			?.let(idFactory)


	override fun serializeId(id: TId) =
		prefix + id.value


	override fun toString() =
		namespace
}
