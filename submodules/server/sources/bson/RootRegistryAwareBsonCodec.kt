@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")

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


	fun <Key : Any, Value : Any> BsonDecoder.readValueMapOfType(name: String, keyClass: KClass<Key>, valueClass: KClass<Value>): Map<Key, Value> {
		readName(name)
		return readValueMapOfType(keyClass = keyClass, valueClass = valueClass)
	}


	fun <Key : Any, Value : Any> BsonDecoder.readValueMapOfTypeOrNull(name: String, keyClass: KClass<Key>, valueClass: KClass<Value>): Map<Key, Value>? {
		readName(name)
		return readValueMapOfTypeOrNull(keyClass = keyClass, valueClass = valueClass)
	}


	fun <Key : Any, Value : Any> BsonDecoder.readValueMapOfType(keyClass: KClass<Key>, valueClass: KClass<Value>): Map<Key, Value> {
		val map = mutableMapOf<Key, Value>()

		readDocument {
			while (readBsonType() != BsonType.END_OF_DOCUMENT) {
				val key = readValueOfType(keyClass)
				map[key] = readValueOfType(valueClass)
			}
		}

		return map
	}


	fun <Key : Any, Value : Any> BsonDecoder.readValueMapOfTypeOrNull(keyClass: KClass<Key>, valueClass: KClass<Value>): Map<Key, Value>? {
		expectValue("readValueMapOfTypeOrNull")

		if (currentBsonType == BsonType.NULL) {
			skipValue()
			return null
		}

		return readValueMapOfType(keyClass = keyClass, valueClass = valueClass)
	}


	fun <Value : Any> BsonDecoder.readValueOfType(name: String, `class`: KClass<Value>): Value {
		readName(name)
		return readValueOfType(`class`)
	}


	fun <Value : Any> BsonDecoder.readValueOfType(`class`: KClass<Value>) =
		rootRegistry[`class`.boxed.java].decode(this, decoderContext)!!


	fun <Value : Any> BsonDecoder.readValueOfTypeOrNull(name: String, `class`: KClass<Value>): Value? {
		readName(name)
		return readValueOfTypeOrNull(`class`)
	}


	fun <Value : Any> BsonDecoder.readValueOfTypeOrNull(`class`: KClass<Value>): Value? {
		expectValue("readValueOfTypeOrNull")

		if (currentBsonType == BsonType.NULL) {
			skipValue()
			return null
		}

		return readValueOfType(`class`)
	}


	fun <Value : Any> BsonDecoder.readValuesOfType(`class`: KClass<Value>): List<Value> =
		readValuesOfType(`class`, container = mutableListOf())


	fun <Value, Container> BsonDecoder.readValuesOfType(`class`: KClass<Value>, container: Container): Container where Value : Any, Container : MutableCollection<Value> {
		readArrayWithValues {
			container.add(readValueOfType(`class`))
		}

		return container
	}


	fun <Value : Any> BsonDecoder.readValuesOfTypeOrNull(`class`: KClass<Value>): List<Value>? {
		expectValue("readValuesOfTypeOrNull")

		if (currentBsonType == BsonType.NULL) {
			skipValue()
			return null
		}

		return readValuesOfType(`class`)
	}


	fun <Value, Container> BsonDecoder.readValuesOfTypeOrNull(`class`: KClass<Value>, container: Container): Container? where Value : Any, Container : MutableCollection<Value> {
		expectValue("readValuesOfTypeOrNull")

		if (currentBsonType == BsonType.NULL) {
			skipValue()
			return null
		}

		return readValuesOfType(`class`, container = container)
	}


	fun BsonEncoder.write(name: String, value: Any?, skipIfNull: Boolean = true) {
		if (skipIfNull && value == null) return

		writeName(name)
		if (value != null) writeValue(value)
		else writeNull()
	}


	fun BsonEncoder.write(name: String, values: Iterable<Any>?, skipIfNull: Boolean = true) {
		if (skipIfNull && values == null) return

		writeName(name)
		if (values != null) writeValues(values)
		else writeNull()
	}


	fun BsonEncoder.write(name: String, values: Map<out Any, Any>?, skipIfNull: Boolean = true) {
		if (skipIfNull && values == null) return

		writeName(name)
		if (values != null) writeValues(values)
		else writeNull()
	}


	fun BsonEncoder.writeValue(value: Any) {
		@Suppress("UNCHECKED_CAST")
		(rootRegistry[value::class.java] as Encoder<Any>).encode(this, value, encoderContext)
	}


	fun BsonEncoder.writeValues(values: Iterable<Any>) {
		writeArray {
			for (value in values) {
				writeValue(value)
			}
		}
	}


	fun BsonEncoder.writeValues(values: Map<out Any, Any>) {
		writeDocument {
			for ((entryKey, entryValue) in values.entries) {
				writeValue(entryKey)
				writeValue(entryValue)
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
