package team.genki.chotto.server

import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import kotlin.reflect.KClass


interface BsonCodec<Value : Any, in Context : BsonCodingContext> : BsonCodecProvider<Context>, Codec<Value> {

	override fun decode(reader: BsonReader, decoderContext: DecoderContext): Value
	override fun encode(writer: BsonWriter, value: Value, encoderContext: EncoderContext)


	@Suppress("UNCHECKED_CAST")
	override fun <Value : Any> codecForClass(valueClass: KClass<in Value>) =
		if (encoderClass == valueClass.java)
			this as BsonCodec<Value, Context>
		else
			null
}
