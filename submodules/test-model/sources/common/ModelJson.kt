package tests


expect object ModelJson {

	inline fun <reified Value : Any> parse(json: String): Value
	inline fun <reified Value : Any> parseOrNull(json: String): Value?
	fun serialize(value: Any): String
}
