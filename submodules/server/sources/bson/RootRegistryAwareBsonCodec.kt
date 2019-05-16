package team.genki.chotto.server

import org.bson.*
import org.bson.codecs.*
import org.bson.codecs.configuration.*
import kotlin.reflect.*
import java.lang.Boolean as BoxedBoolean
import java.lang.Byte as BoxedByte
import java.lang.Character as BoxedCharacter
import java.lang.Double as BoxedDouble
import java.lang.Float as BoxedFloat
import java.lang.Integer as BoxedInteger
import java.lang.Long as BoxedLong
import java.lang.Short as BoxedShort
import java.lang.Void as BoxedVoid


interface RootRegistryAwareBsonCodec {

	val rootRegistry: CodecRegistry


	fun <Value : Any> BsonReader.readValueOfType(name: String, `class`: KClass<Value>): Value {
		readName(name)
		return readValueOfType(`class`)
	}


	fun <Value : Any> BsonReader.readValueOfType(`class`: KClass<Value>) =
		rootRegistry[`class`.boxed.java].decode(this, decoderContext)!!


	fun <Value : Any> BsonReader.readValueOfTypeOrNull(name: String, `class`: KClass<Value>): Value? {
		readName(name)
		return readValueOfTypeOrNull(`class`)
	}


	fun <Value : Any> BsonReader.readValueOfTypeOrNull(`class`: KClass<Value>): Value? {
		expectValue("readValueOfTypeOrNull")

		if (currentBsonType == BsonType.NULL) {
			skipValue()
			return null
		}

		return readValueOfType(`class`)
	}


	fun <Value : Any> BsonReader.readValuesOfType(`class`: KClass<Value>): List<Value> =
		readValuesOfType(`class`, container = mutableListOf())


	fun <Value, Container> BsonReader.readValuesOfType(`class`: KClass<Value>, container: Container): Container where Value : Any, Container : MutableCollection<Value> {
		readArrayWithValues {
			container.add(readValueOfType(`class`))
		}

		return container
	}


	fun <Value : Any> BsonReader.readValuesOfTypeOrNull(`class`: KClass<Value>): List<Value>? {
		expectValue("readValuesOfTypeOrNull")

		if (currentBsonType == BsonType.NULL) {
			skipValue()
			return null
		}

		return readValuesOfType(`class`)
	}


	fun <Value, Container> BsonReader.readValuesOfTypeOrNull(`class`: KClass<Value>, container: Container): Container? where Value : Any, Container : MutableCollection<Value> {
		expectValue("readValuesOfTypeOrNull")

		if (currentBsonType == BsonType.NULL) {
			skipValue()
			return null
		}

		return readValuesOfType(`class`, container = container)
	}


	fun BsonWriter.write(name: String, value: Any?, skipIfNull: Boolean = true) {
		if (skipIfNull && value == null) return

		writeName(name)
		if (value != null) writeValue(value)
		else writeNull()
	}


	fun BsonWriter.write(name: String, values: Iterable<Any>?, skipIfNull: Boolean = true) {
		if (skipIfNull && values == null) return

		writeName(name)
		if (values != null) writeValues(values)
		else writeNull()
	}


	fun BsonWriter.writeValue(value: Any) {
		@Suppress("UNCHECKED_CAST")
		(rootRegistry[value::class.java] as Encoder<Any>).encode(this, value, encoderContext)
	}


	fun BsonWriter.writeValues(values: Iterable<Any>) {
		writeArray {
			for (value in values) {
				writeValue(value)
			}
		}
	}


	companion object {

		private val decoderContext = DecoderContext.builder().build()!!
		private val encoderContext = EncoderContext.builder().build()!!
	}
}


@Suppress("UNCHECKED_CAST")
private val <T : Any> KClass<T>.boxed
	get() =
		if (java.isPrimitive)
			when (this) {
				Boolean::class -> BoxedBoolean::class
				Byte::class -> BoxedByte::class
				Char::class -> BoxedCharacter::class
				Double::class -> BoxedDouble::class
				Float::class -> BoxedFloat::class
				Int::class -> BoxedInteger::class
				Long::class -> BoxedLong::class
				Short::class -> BoxedShort::class
				else -> BoxedVoid::class
			} as KClass<T>
		else
			this
