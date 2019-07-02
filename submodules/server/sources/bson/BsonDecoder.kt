package team.genki.chotto.server

import org.bson.*
import org.bson.AbstractBsonReader.*


class BsonDecoder internal constructor(private val delegate: AbstractBsonReader) : BsonReader by delegate {

	fun expectValue(methodName: String) {
		var state = delegate.state
		if (state == State.INITIAL || state == State.SCOPE_DOCUMENT || state == State.TYPE) {
			readBsonType()
		}

		state = delegate.state
		if (state == State.NAME) {
			skipName()
		}

		state = delegate.state
		if (state != State.VALUE) {
			throw BsonInvalidOperationException("$methodName can only be called when State is ${State.VALUE}, not when State is $state.")
		}
	}


	override fun readString(): String =
		if (delegate.state == State.NAME) delegate.readName()
		else delegate.readString()


	companion object {

		internal fun wrap(reader: BsonReader) =
			when (reader) {
				is BsonDecoder -> reader
				is AbstractBsonReader -> BsonDecoder(reader)
				else -> error("Cannot wrap BsonReader of ${reader::class}")
			}
	}
}


inline fun <T> BsonDecoder.readArray(name: String, read: BsonDecoder.() -> T): T {
	readName(name)

	return readArray(read)
}


inline fun <T> BsonDecoder.readArray(read: BsonDecoder.() -> T): T {
	readStartArray()
	val result = read()
	readEndArray()

	return result
}


inline fun BsonDecoder.readArrayWithValues(readValue: BsonDecoder.() -> Unit) {
	readArray {
		while (readBsonType() != BsonType.END_OF_DOCUMENT) {
			readValue()
		}
	}
}


fun BsonDecoder.readBooleanOrNull(): Boolean? {
	expectValue("readBooleanOrNull")

	if (currentBsonType == BsonType.NULL) {
		skipValue()
		return null
	}

	return readBoolean()
}


fun BsonDecoder.readCoercedDouble(): Double {
	expectValue("readCoercedDouble")

	return when (currentBsonType) {
		BsonType.INT32 -> readInt32().toDouble()
		BsonType.INT64 -> readInt64().toDouble()
		else -> readDouble()
	}
}


fun BsonDecoder.readCoercedInt32(): Int {
	expectValue("readCoercedInt32")

	return when (currentBsonType) {
		BsonType.DOUBLE -> readDouble().toInt()
		BsonType.INT64 -> readInt64().toInt()
		else -> readInt32()
	}
}


fun BsonDecoder.readCoercedInt32OrNull(): Int? {
	expectValue("readCoercedInt32OrNull")

	if (currentBsonType == BsonType.NULL) {
		skipValue()
		return null
	}

	return readCoercedInt32()
}


inline fun <T> BsonDecoder.readDocument(read: BsonDecoder.() -> T): T {
	readStartDocument()
	val result = read()
	readEndDocument()

	return result
}


inline fun <T> BsonDecoder.readDocument(name: String, read: BsonDecoder.() -> T): T {
	readName(name)

	return readDocument(read)
}


inline fun <T> BsonDecoder.readDocumentOrNull(read: BsonDecoder.() -> T): T? {
	expectValue("readDocumentOrNull")

	if (currentBsonType == BsonType.NULL) {
		skipValue()
		return null
	}

	return readDocument(read)
}


inline fun <T> BsonDecoder.readDocumentOrNull(name: String, read: BsonDecoder.() -> T): T? {
	readName(name)

	return readDocumentOrNull(read)
}


inline fun BsonDecoder.readDocumentWithValues(readValue: BsonDecoder.(fieldName: String) -> Unit) {
	readDocument {
		while (readBsonType() != BsonType.END_OF_DOCUMENT) {
			readValue(readName())
		}
	}
}


fun BsonDecoder.readInt32OrNull(): Int? {
	expectValue("readInt32OrNull")

	if (currentBsonType == BsonType.NULL) {
		skipValue()
		return null
	}

	return readInt32()
}


inline fun <Key, Value> BsonDecoder.readMap(readEntry: BsonDecoder.(fieldName: String) -> Pair<Key, Value>): Map<Key, Value> {
	val map = mutableMapOf<Key, Value>()
	readDocumentWithValues { fieldName ->
		map += readEntry(fieldName)
	}
	return map
}


fun BsonDecoder.readStringOrNull(): String? {
	expectValue("readStringOrNull")

	if (currentBsonType == BsonType.NULL) {
		skipValue()
		return null
	}

	return readString()
}
