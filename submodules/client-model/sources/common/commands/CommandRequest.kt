package team.genki.chotto.client.model


data class CommandRequest<out Command : team.genki.chotto.client.model.Command<*>, out Meta : CommandRequest.Meta>(
	val command: Command,
	val meta: Meta
) {

	interface Meta
}
