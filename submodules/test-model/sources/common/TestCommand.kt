package tests

import team.genki.chotto.core.*
import tests.TestCommand.*


data class TestCommand(
	val property: String
) : Command.Typed<TestCommand, Result>(Companion) {

	data class Result(
		val property: String
	)


	companion object : Descriptor<TestCommand, Result> by commandDescriptor("test")
}
