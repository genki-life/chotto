package team.genki.chotto.client

import io.ktor.client.engine.*
import io.ktor.http.*
import team.genki.chotto.core.*


expect class ChottoClient<TModel : ClientModel<*, *>>(
	baseUrl: Url,
	httpEngine: HttpClientEngineFactory<*> = defaultHttpEngine,
	model: TModel
) {

	companion object {

		internal val defaultHttpEngine: HttpClientEngineFactory<*>
	}
}
