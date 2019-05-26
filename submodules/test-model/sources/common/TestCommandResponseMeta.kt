package tests

import kotlinx.serialization.*
import team.genki.chotto.core.*


@Serializable
data class TestCommandResponseMeta(
	val property: String?
) : CommandResponseMeta
