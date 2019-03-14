package team.genki.chotto.client.model

import kotlin.reflect.KClass


interface EntityType<Id : EntityId, Entity : team.genki.chotto.client.model.Entity> {

	val entityClass: KClass<Entity>
	val idClass: KClass<Id>
	val namespace: String

	fun parseId(string: String): Id?
	fun serializeId(id: Id): String

	fun Id.serialize(): String =
		serializeId(this)


	companion object {

		@PublishedApi
		internal fun <Id : EntityId, Entity : team.genki.chotto.client.model.Entity> create(
			entityClass: KClass<Entity>,
			namespace: String,
			idClass: KClass<Id>,
			idFactory: (value: String) -> Id
		): EntityType<Id, Entity> =
			ActualEntityType(
				entityClass = entityClass,
				idClass = idClass,
				idFactory = idFactory,
				namespace = namespace
			)
	}
}


inline infix fun <reified Id : EntityId, reified Entity : team.genki.chotto.client.model.Entity> String.using(
	noinline idFactory: (value: String) -> Id
): EntityType<Id, Entity> =
	EntityType.create(
		entityClass = Entity::class,
		idClass = Id::class,
		idFactory = idFactory,
		namespace = this
	)


private class ActualEntityType<Id : EntityId, Entity : team.genki.chotto.client.model.Entity>(
	override val entityClass: KClass<Entity>,
	override val idClass: KClass<Id>,
	private val idFactory: (value: String) -> Id,
	override val namespace: String
) : EntityType<Id, Entity> {

	internal val prefix = "$namespace/"


	override fun parseId(string: String) =
		string
			.takeIf { it.startsWith(prefix) || !it.contains('/') }
			?.removePrefix(prefix)
			?.let(idFactory)


	override fun serializeId(id: Id) =
		prefix + id.value
}
