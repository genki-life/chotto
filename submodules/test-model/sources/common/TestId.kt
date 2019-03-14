package tests

import team.genki.chotto.client.model.*


data /*inline*/ class TestId(override val value: String) : EntityId {

	override val type: Companion
		get() = Companion


	override fun toString() =
		serialize()


	companion object : EntityType<TestId, TestEntity> by "tests" using ::TestId
}
