package team.genki.chotto.server

import team.genki.chotto.core.*
import java.util.*


internal class CommandHandler(
	handlers: Collection<SpecificCommandHandler<*, *>>
) {

	private val handlerByDefinition =
		handlers.associateTo(IdentityHashMap()) { it.definition to it.handler }


	@Suppress("UNCHECKED_CAST")
	suspend fun handle(command: Command): Any {
		val definition = command.definition
		val handle = handlerByDefinition[definition]
			?.let { it as suspend (command: Command) -> Any }
			?: error("no handler registered for command '$definition'")

		return handle(command)
	}
}
