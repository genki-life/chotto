package team.genki.chotto.server

import io.ktor.application.*


interface ChottoEnvironment<Context : ChottoServerContext, Transaction : ChottoTransaction> {

	suspend fun createContext(): Context
	suspend fun createTransaction(context: Context, call: ApplicationCall?): Transaction
	suspend fun onStart(context: Context) {}
}
