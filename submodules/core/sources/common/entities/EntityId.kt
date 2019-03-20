package team.genki.chotto.core


interface EntityId {

	val type: EntityType<*, *>
	val value: String


	interface Typed<TId : Typed<TId, TEntity>, TEntity : Entity.Typed<TId, TEntity>> : EntityId {

		override val type: EntityType<TId, TEntity>
	}
}
