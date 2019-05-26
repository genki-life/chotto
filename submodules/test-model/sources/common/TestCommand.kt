package tests

import kotlinx.serialization.*
import team.genki.chotto.core.*


@Serializable
data class TestCommand(
	val property: String
) : TypedCommand<TestCommand, TestCommandResult>() {

	override val definition get() = Meta.definition


	companion object Meta : TypedCommandMeta<TestCommand, TestCommandResult> {

		override val definition = TypedCommandDefinition(
			name = "test",
			resultSerializer = TestCommandResult.serializer(),
			serializer = serializer()
		)
	}
}


@Serializable
data class TestCommandResult(
	val property: String
)
