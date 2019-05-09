package team.genki.chotto.server

import team.genki.chotto.core.*
import java.util.*


internal class CommandHandler(
	handlers: Collection<SpecificCommandHandler<*, *>>
) {

	private val handlerByDescriptor =
		handlers.associateTo(IdentityHashMap()) { it.descriptor to it.handler }


	@Suppress("UNCHECKED_CAST")
	suspend fun handle(command: Command): Any {
		val descriptor = command.descriptor
		val handle = handlerByDescriptor[descriptor]
			?.let { it as suspend (command: Command) -> Any }
			?: error("no handler registered for command '${descriptor.name}' (${descriptor.commandClass.qualifiedName})")

		return handle(command)
	}
}
