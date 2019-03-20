package tests

import team.genki.chotto.core.*


data class TestEntity(
	override val id: TestId,
	val property: String
) : Entity.Typed<TestId, TestEntity>
