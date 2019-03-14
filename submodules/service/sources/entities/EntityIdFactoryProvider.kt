package team.genki.chotto


internal interface EntityIdFactoryProvider {

	fun idFactoryForType(type: String): EntityId.Factory<*>?
}
