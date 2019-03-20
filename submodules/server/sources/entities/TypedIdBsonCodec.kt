package team.genki.chotto.server

import org.bson.BsonReader
import org.bson.BsonWriter
import team.genki.chotto.core.*


internal class TypedIdBsonCodec(types: Collection<EntityType<*, *>>) : AbstractBsonCodec<TypedId, BsonCodingContext>() {

	private val typeByNamespace = types.associateBy { it.namespace }


	override fun BsonReader.decode(context: BsonCodingContext) =
		readDocument {
			val factory = readString("type").let { namespace ->
				typeByNamespace[namespace] ?: error("ID type '$namespace' has not been registered with Chotto")
			}

			readName("id")
			readValueOfType(factory.idClass).typed
		}


	override fun BsonWriter.encode(value: TypedId, context: BsonCodingContext) {
		writeDocument {
			write("type", string = value.untyped.type.namespace)
			write("id", value = value.untyped)
		}
	}
}
