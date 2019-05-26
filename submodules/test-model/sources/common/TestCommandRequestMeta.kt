package tests

import kotlinx.serialization.*
import team.genki.chotto.core.*


@Serializable
data class TestCommandRequestMeta(
	val property: String?
) : CommandRequestMeta
