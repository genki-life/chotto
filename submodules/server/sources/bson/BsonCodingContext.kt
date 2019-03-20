package team.genki.chotto.server


interface BsonCodingContext {

	companion object {

		val empty = object : BsonCodingContext {}
	}
}
