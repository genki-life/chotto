package team.genki.chotto.server

import team.genki.chotto.core.*


inline class TypedId(val untyped: EntityId) {

	override fun toString() =
		untyped.toString()
}


val EntityId.typed
	get() = TypedId(this)
