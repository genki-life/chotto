package tests

import team.genki.chotto.core.*


data /*inline*/ class TestId(override val value: String) : EntityId.Typed<TestId, TestEntity> {

	override val type: Companion
		get() = Companion


	override fun toString() =
		serialize()


	companion object : EntityType<TestId, TestEntity> by "tests" using ::TestId
}
