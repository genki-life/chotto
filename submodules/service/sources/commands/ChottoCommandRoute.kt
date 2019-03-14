package team.genki.chotto

import io.ktor.routing.Route


internal class ChottoCommandRoute<in Transaction : ChottoTransaction>(
	val factory: ChottoCommandFactory<Transaction, *, *>,
	val route: Route
)
