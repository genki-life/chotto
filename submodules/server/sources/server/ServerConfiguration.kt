package team.genki.chotto.server


internal class ServerConfiguration<Context : ChottoServerContext, Transaction : ChottoTransaction>(
	val context: Context,
	val environment: ChottoEnvironment<Context, Transaction>,
	val modules: List<ChottoModule<in Context, in Transaction>>
) {

	suspend fun createCall(): ChottoCall<Transaction> {
		val transactionController = environment.createTransactionController(context = context)
		val transaction = transactionController.transaction
		val transactionConfigurations = modules.map { it.configurationForTransaction(transaction) }

		val commandHandlers = transactionConfigurations
			.flatMap { it.commands.handlers }
			.also { handlers ->
				handlers.groupBy { it.definition }
					.values
					.firstOrNull { it.size > 1 }
					?.let { it.first().definition }
					?.let { definition ->
						error("Multiple handlers registered for command '$definition'")
					}
			}

		val entityResolvers = transactionConfigurations
			.flatMap { it.entities.resolvers }
			.also { resolvers ->
				resolvers.groupBy { it.type }
					.values
					.firstOrNull { it.size > 1 }
					?.let { it.first().type }
					?.let { type ->
						error("Multiple resolvers registered for entity type '$type'")
					}
			}

		return ChottoCall(
			commandHandler = CommandHandler(handlers = commandHandlers),
			entityResolver = EntityResolver(resolvers = entityResolvers),
			transactionController = transactionController
		)
	}
}
