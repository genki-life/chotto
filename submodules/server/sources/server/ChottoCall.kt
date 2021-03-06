package team.genki.chotto.server


internal class ChottoCall<Transaction : ChottoTransaction>(
	val commandHandler: CommandHandler,
	val entityResolver: EntityResolver<Transaction>,
	val transactionController: ChottoTransaction.Controller<Transaction>
)
