package team.genki.chotto.client


internal actual fun CommandExecutor.decodeString(data: ByteArray) =
	data.toString(Charsets.UTF_8)


internal actual fun CommandExecutor.encodeString(string: String) =
	string.toByteArray(Charsets.UTF_8)
