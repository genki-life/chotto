package team.genki.chotto


inline class TypedId(val untyped: EntityId) {

	override fun toString() =
		untyped.toString()
}
