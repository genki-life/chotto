package team.genki.chotto.client.model


data class CommandResponse<out Result : Any, out Meta : CommandResponse.Meta>(
	val entities: Map<EntityId, Entity>,
	val meta: Meta,
	val result: Result
) {

	interface Meta
}
