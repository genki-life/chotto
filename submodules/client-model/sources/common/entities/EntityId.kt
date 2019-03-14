package team.genki.chotto.client.model


interface EntityId {

	val type: EntityType<*, *>
	val value: String
}
