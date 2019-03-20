package tests

import team.genki.chotto.core.*


object TestClientModel : ClientModel<TestCommandRequestMeta, TestCommandResponseMeta>(
	name = "test",
	commandDescriptors = listOf(TestCommand),
	commandRequestMetaClass = TestCommandRequestMeta::class,
	commandResponseMetaClass = TestCommandResponseMeta::class,
	entityTypes = listOf(TestId),
	jsonConfiguration = testJsonConfiguration
) {

	override fun createDefaultResponseMeta() = TestCommandResponseMeta(
		property = null
	)


	override fun createRequestMetaForCommand(command: Command) = TestCommandRequestMeta(
		property = "value"
	)
}


expect val testJsonConfiguration: JsonConfiguration
