package team.genki.chotto.server

import team.genki.chotto.core.*


internal class SpecificCommandHandler<TCommand : Command.Typed<TCommand, Result>, Result : Any>(
	val descriptor: Command.Typed.Descriptor<out TCommand, out Result>,
	val handler: suspend (command: TCommand) -> Result
)
