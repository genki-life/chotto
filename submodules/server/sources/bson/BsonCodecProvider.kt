package team.genki.chotto.server

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass


interface BsonCodecProvider<in Context : BsonCodingContext> {

	fun <Value : Any> codecForClass(valueClass: KClass<in Value>): BsonCodec<Value, Context>?
}


fun <Context : BsonCodingContext> BsonCodecProvider(
	vararg providers: BsonCodecProvider<Context>
) =
	BsonCodecProvider(providers.asIterable())


fun <Context : BsonCodingContext> BsonCodecProvider(
	providers: Iterable<BsonCodecProvider<Context>>
): BsonCodecProvider<Context> =
	BsonCodecProviderCollection(providers = providers)


private class BsonCodecProviderCollection<in Context : BsonCodingContext>(
	providers: Iterable<BsonCodecProvider<Context>>
) : BsonCodecProvider<Context> {

	private val codecByClass = ConcurrentHashMap<KClass<*>, BsonCodec<*, Context>>()
	private val providers = providers.toSet().toTypedArray()


	@Suppress("UNCHECKED_CAST")
	override fun <Value : Any> codecForClass(valueClass: KClass<in Value>): BsonCodec<Value, Context>? {
		return codecByClass.getOrPut(valueClass) {
			for (provider in providers) {
				val codec = provider.codecForClass(valueClass)
				if (codec != null) {
					return@getOrPut codec
				}
			}

			return null
		} as BsonCodec<Value, Context>
	}
}
