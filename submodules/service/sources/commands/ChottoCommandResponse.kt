package team.genki.chotto


internal data class ChottoCommandResponse(
	val factory: ChottoCommandFactory<*, *, *>,
	val result: Any
)
