package team.genki.chotto

import io.ktor.application.ApplicationCall


interface ChottoService<Context : ChottoContext, Transaction : ChottoTransaction> {

	suspend fun createContext(): Context
	suspend fun createTransaction(context: Context, call: ApplicationCall?): Transaction
	suspend fun onStart(context: Context) {}
}
