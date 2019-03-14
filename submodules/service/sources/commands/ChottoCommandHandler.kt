package team.genki.chotto


internal class ChottoCommandHandler<in Transaction : ChottoTransaction, Command : ChottoCommand, Result : Any>(
	val factory: ChottoCommandFactory<Transaction, Command, Result>,
	val handler: Transaction.() -> (suspend (command: Command) -> Result)
)
