package team.genki.chotto.core


interface Entity {

	val id: EntityId


	companion object


	interface Typed<TId : EntityId.Typed<TId, TEntity>, TEntity : Typed<TId, TEntity>> : Entity {

		override val id: TId
	}
}
