package team.genki.chotto.server

import team.genki.chotto.core.*


internal class SpecificCommandHandler<TCommand : TypedCommand<TCommand, Result>, Result : Any>(
	val definition: TypedCommandDefinition<out TCommand, out Result>,
	val handler: suspend (command: TCommand) -> Result
)
