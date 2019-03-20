package team.genki.chotto.server

import team.genki.chotto.core.*


interface ServerEntity {

	val id: EntityId


	interface Typed<
		TId : EntityId.Typed<TId, TEntity>,
		TEntity : Entity.Typed<TId, TEntity>,
		in TTransaction : ChottoTransaction
		> : ServerEntity, CoreModelConvertible<TEntity, TTransaction> {

		override val id: TId
	}
}
