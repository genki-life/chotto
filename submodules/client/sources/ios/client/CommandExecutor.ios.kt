package team.genki.chotto.client


internal actual fun CommandExecutor.decodeString(data: ByteArray) =
	data.stringFromUtf8()


internal actual fun CommandExecutor.encodeString(string: String) =
	string.toUtf8()
