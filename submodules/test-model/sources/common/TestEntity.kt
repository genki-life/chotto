package tests

import kotlinx.serialization.*
import team.genki.chotto.core.*


@Serializable
data class TestEntity(
	override val id: TestId,
	val property: String
) : Entity.Typed<TestId, TestEntity>
