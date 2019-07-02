package team.genki.chotto.server

import kotlinx.io.core.*
import org.bson.*


class BsonEncoder internal constructor(private val delegate: AbstractBsonWriter) : BsonWriter by delegate, Closeable by delegate {

	override fun writeString(value: String) {
		if (AbstractBsonWriter_getState(delegate) == AbstractBsonWriter.State.NAME) writeName(value)
		else delegate.writeString(value)
	}


	companion object {

		private val AbstractBsonWriter_getState = AbstractBsonWriter::class.java.declaredMethods.single { it.name == "getState" }


		init {
			AbstractBsonWriter_getState.isAccessible = true
		}


		internal fun wrap(writer: BsonWriter) =
			when (writer) {
				is BsonEncoder -> writer
				is AbstractBsonWriter -> BsonEncoder(writer)
				else -> error("Cannot wrap BsonWriter of ${writer::class}")
			}
	}
}


fun BsonEncoder.write(name: String, boolean: Boolean) {
	writeName(name)
	writeBoolean(boolean)
}


inline fun BsonEncoder.write(name: String, write: BsonEncoder.() -> Unit) {
	writeName(name)
	writeDocument(write = write)
}


inline fun <Value : Any> BsonEncoder.write(name: String, document: Value, write: BsonEncoder.(value: Value) -> Unit) {
	writeName(name)
	writeDocument(document = document, write = write)
}


@JvmName("writeOrSkip")
inline fun <Value : Any> BsonEncoder.write(name: String, documentOrSkip: Value?, write: BsonEncoder.(value: Value) -> Unit) {
	documentOrSkip ?: return

	write(name = name, document = documentOrSkip, write = write)
}


fun BsonEncoder.write(name: String, double: Double) {
	writeName(name)
	writeDouble(double)
}


@JvmName("writeOrSkip")
fun BsonEncoder.write(name: String, doubleOrSkip: Double?) {
	if (doubleOrSkip == null) {
		return
	}

	write(name = name, double = doubleOrSkip)
}


fun BsonEncoder.write(name: String, int32: Int) {
	writeName(name)
	writeInt32(int32)
}


@JvmName("writeOrSkip")
fun BsonEncoder.write(name: String, int32OrSkip: Int?) {
	if (int32OrSkip == null) {
		return
	}

	write(name = name, int32 = int32OrSkip)
}


fun BsonEncoder.write(name: String, string: String) {
	writeName(name)
	writeString(string)
}


fun BsonEncoder.write(name: String, strings: Iterable<String>) {
	writeName(name)
	writeStrings(strings)
}


inline fun <K, V> BsonEncoder.write(name: String, value: Map<K, V>, writeEntry: (entry: Map.Entry<K, V>) -> Unit) {
	writeName(name)
	writeDocument {
		value.entries.forEach(writeEntry)
	}
}


@JvmName("writeOrSkip")
inline fun <K, V> BsonEncoder.write(name: String, valueOrSkip: Map<K, V>?, writeEntry: (entry: Map.Entry<K, V>) -> Unit) {
	valueOrSkip ?: return

	write(name = name, value = valueOrSkip, writeEntry = writeEntry)
}


fun BsonEncoder.write(name: String, stringOrSkip: String?, skipIfEmpty: Boolean = false) {
	if (stringOrSkip == null || (skipIfEmpty && stringOrSkip.isEmpty())) {
		return
	}

	write(name = name, string = stringOrSkip)
}


inline fun BsonEncoder.writeArray(name: String, write: BsonEncoder.() -> Unit) {
	writeName(name)
	writeArray(write)
}


inline fun BsonEncoder.writeArray(write: BsonEncoder.() -> Unit) {
	writeStartArray()
	write()
	writeEndArray()
}


inline fun BsonEncoder.writeDocument(write: BsonEncoder.() -> Unit) {
	writeStartDocument()
	write()
	writeEndDocument()
}


inline fun <Value : Any> BsonEncoder.writeDocument(document: Value, write: BsonEncoder.(value: Value) -> Unit) {
	writeDocument {
		write(document)
	}
}


inline fun <Key, Value> BsonEncoder.writeMap(map: Map<Key, Value>, writeEntry: BsonEncoder.(key: Key, value: Value) -> Unit) {
	writeDocument {
		for ((key, value) in map) {
			writeEntry(key, value)
		}
	}
}


fun BsonEncoder.writeStrings(strings: Iterable<String>) {
	writeStartArray()
	for (string in strings) {
		writeString(string)
	}
	writeEndArray()
}
