package team.genki.chotto.server


interface ChottoEnvironment<Context : ChottoServerContext, Transaction : ChottoTransaction> {

	suspend fun createContext(): Context
	suspend fun createTransactionController(context: Context): ChottoTransaction.Controller<Transaction>
	suspend fun onStart(context: Context) {}
}
