package tests

import kotlinx.serialization.*
import team.genki.chotto.core.*


@Serializable
data class TestCommand(
	val property: String
) : Command.Typed<TestCommand, TestCommandResult>() {

	override val descriptor get() = Companion

	companion object : Descriptor<TestCommand, TestCommandResult> by commandDescriptor("test")
}


@Serializable
data class TestCommandResult(
	val property: String
)
