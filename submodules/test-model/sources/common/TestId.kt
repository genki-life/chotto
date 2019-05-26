package tests

import kotlinx.serialization.*
import team.genki.chotto.core.*


@Serializable(with = EntityIdSerializer::class)
data /*inline*/ class TestId(override val value: String) : EntityId.Typed<TestId, TestEntity> {

	override val type
		get() = Companion


	override fun toString() =
		serialize()


	companion object : EntityType.Typed<TestId, TestEntity> by "tests" using ::TestId
}
