package team.genki.chotto.core

import team.genki.chotto.core.CommandResponse.*


data class CommandResponse<out TResult : Any, out TMeta : Meta>(
	val entities: Map<EntityId, Entity>,
	val meta: TMeta,
	val result: TResult
) {

	companion object


	interface Meta
}
