package team.genki.chotto

import org.bson.BsonReader
import org.bson.BsonWriter


internal class EntityIdBSONCodec<Id : EntityId>(
	private val factory: EntityId.Factory<Id>
) : AbstractBSONCodec<Id, BSONCodingContext>(valueClass = factory.idClass.java) {

	override fun BsonReader.decode(context: BSONCodingContext) =
		factory.run { readIdValue() }


	override fun BsonWriter.encode(value: Id, context: BSONCodingContext) {
		factory.run { writeIdValue(value) }
	}
}
