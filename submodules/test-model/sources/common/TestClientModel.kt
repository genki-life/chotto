package tests

import team.genki.chotto.core.*


object TestClientModel : ClientModel<TestCommandRequestMeta, TestCommandResponseMeta>(
	name = "test",
	commandDescriptors = setOf(TestCommand),
	commandRequestMetaClass = TestCommandRequestMeta::class,
	commandResponseMetaClass = TestCommandResponseMeta::class,
	entityTypes = setOf(TestId)
) {

	override fun createDefaultResponseMeta() = TestCommandResponseMeta(
		property = null
	)


	override fun createRequestMetaForCommand(command: Command) = TestCommandRequestMeta(
		property = "value"
	)
}
