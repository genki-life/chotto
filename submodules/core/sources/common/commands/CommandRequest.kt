package team.genki.chotto.core

import team.genki.chotto.core.CommandRequest.*


data class CommandRequest<out TCommand : Command.Typed<*, *>, out TMeta : Meta>(
	val command: TCommand,
	val meta: TMeta
) {

	interface Meta


	sealed class Status<TResult : Any, TMeta : CommandResponse.Meta> {

		data class Failure<TResult : Any, TMeta : CommandResponse.Meta>(val cause: CommandFailure) : Status<TResult, TMeta>()
		data class Success<TResult : Any, TMeta : CommandResponse.Meta>(val response: CommandResponse<TResult, TMeta>) : Status<TResult, TMeta>()
	}
}
