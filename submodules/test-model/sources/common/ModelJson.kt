package tests


expect object ModelJson {

	inline fun <reified Value : Any> parse(json: String): Value
	fun serialize(value: Any): String
}
