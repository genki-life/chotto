package team.genki.chotto.server

import org.bson.*
import org.bson.types.*
import team.genki.chotto.core.*


@Suppress("UNCHECKED_CAST")
internal class SpecificEntityIdBsonCodec(
	private val type: EntityType<*, *>
) : AbstractBsonCodec<EntityId.Typed<*, *>, BsonCodingContext>(valueClass = type.idClass.java as Class<EntityId.Typed<*, *>>) {

	override fun BsonReader.decode(context: BsonCodingContext): EntityId.Typed<*, *> {
		val raw = when (currentBsonType) {
			BsonType.STRING -> readString()
			else -> readObjectId().toHexString()
		}

		return type.parseId(raw) ?: error("invalid '${type.namespace}' ID: $raw")
	}


	override fun BsonWriter.encode(value: EntityId.Typed<*, *>, context: BsonCodingContext) {
		when {
			ObjectId.isValid(value.value) -> writeObjectId(ObjectId(value.value))
			else -> writeString(value.value)
		}
	}
}
