package team.genki.chotto


interface BSONCodingContext {

	companion object {

		val empty = object : BSONCodingContext {}
	}
}
