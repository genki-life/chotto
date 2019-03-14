package tests

import team.genki.chotto.client.model.*


data class TestEntity(
	override val id: TestId,
	val property: String
) : Entity
