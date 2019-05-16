package team.genki.chotto.server

import team.genki.chotto.core.*


interface ChottoTransaction {

	open class Controller<out Transaction : ChottoTransaction>(
		val transaction: Transaction
	) {

		open fun onRequestReceived(request: CommandRequest<*, *>) {}
	}
}
