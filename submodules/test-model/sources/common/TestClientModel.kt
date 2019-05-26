package tests

import team.genki.chotto.core.*


object TestClientModel : ClientModel<TestCommandRequestMeta, TestCommandResponseMeta>(
	name = "test",
	commandDefinitions = setOf(TestCommand),
	commandRequestMetaSerializer = TestCommandRequestMeta.serializer(),
	commandResponseMetaSerializer = TestCommandResponseMeta.serializer(),
	entityTypes = setOf(TestId)
) {

	override fun createDefaultResponseMeta() = TestCommandResponseMeta(
		property = null
	)


	override fun createRequestMetaForCommand(command: Command) = TestCommandRequestMeta(
		property = "value"
	)
}
