package tests

import team.genki.chotto.client.model.*
import tests.TestCommand.Result


data class TestCommand(
	val property: String
) : Command<Result>(Companion) {

	data class Result(
		val property: String
	)


	companion object : Descriptor<TestCommand, Result> by commandDescriptor("test")
}
