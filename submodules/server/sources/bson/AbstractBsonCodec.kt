package team.genki.chotto.server

import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass


abstract class AbstractBsonCodec<Value : Any, in Context : BsonCodingContext>(
	private val additionalProviders: List<BsonCodecProvider<Context>> = emptyList(),
	valueClass: Class<Value>? = null,
	private val includesSubclasses: Boolean = false
) : BsonCodec<Value, Context>, RootRegistryAwareBsonCodec {

	private var _rootRegistry: CodecRegistry? = null

	private var context: Context? = null
	private val valueClass = valueClass ?: defaultValueClass(this::class)


	abstract fun BsonReader.decode(context: Context): Value
	abstract fun BsonWriter.encode(value: Value, context: Context)


	final override fun <Value : Any> codecForClass(valueClass: KClass<in Value>): BsonCodec<Value, Context>? {
		super.codecForClass(valueClass)?.let { return it }

		@Suppress("UNCHECKED_CAST")
		if (includesSubclasses && this.valueClass.isAssignableFrom(valueClass.java))
			return this as BsonCodec<Value, Context>

		for (provider in additionalProviders)
			provider.codecForClass(valueClass)?.let { return it }

		return null
	}


	internal fun configure(context: Context, rootRegistry: CodecRegistry) {
		this.context = context
		this._rootRegistry = rootRegistry
	}


	final override fun decode(reader: BsonReader, decoderContext: DecoderContext) =
		reader.decode(context = requireContext())


	final override fun encode(writer: BsonWriter, value: Value, encoderContext: EncoderContext) =
		writer.encode(value = value, context = requireContext())


	final override fun getEncoderClass() =
		valueClass


	private fun requireContext() =
		context ?: error("AbstractBsonCodec must be used by the CodecRegistry provided by Chotto")


	override val rootRegistry
		get() = _rootRegistry ?: error("AbstractBsonCodec must be used by the CodecRegistry provided by Chotto")
}


@Suppress("UNCHECKED_CAST")
private fun <Value : Any> defaultValueClass(codecClass: KClass<out AbstractBsonCodec<Value, *>>): Class<Value> {
	val typeArgument = (codecClass.java.genericSuperclass as ParameterizedType).actualTypeArguments.first()
	return when (typeArgument) {
		is Class<*> -> typeArgument as Class<Value>
		is ParameterizedType -> typeArgument.rawType as Class<Value>
		else -> error("unsupported type: $typeArgument")
	}
}
