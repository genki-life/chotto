package team.genki.chotto.client

import io.ktor.http.*
import team.genki.chotto.core.*


expect class ChottoClient<TModel : ClientModel<*, *>>(
	baseUrl: Url,
	model: TModel
) {

	companion object
}
