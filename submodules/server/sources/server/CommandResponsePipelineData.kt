package team.genki.chotto.server

import team.genki.chotto.core.*


internal class CommandResponsePipelineData(
	val command: Command,
	val meta: CommandResponseMeta,
	val model: ClientModel<*, *>,
	val result: Any
)
