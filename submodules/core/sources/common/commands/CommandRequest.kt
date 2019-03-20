package team.genki.chotto.core

import team.genki.chotto.core.CommandRequest.*


data class CommandRequest<out TCommand : Command.Typed<*, *>, out TMeta : Meta>(
	val command: TCommand,
	val meta: TMeta
) {

	interface Meta
}
