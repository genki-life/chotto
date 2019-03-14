package tests

import com.github.fluidsonic.fluid.json.*


@JSON.CodecProvider(
	externalTypes = [
		JSON.ExternalType(TestCommand::class),
		JSON.ExternalType(TestCommand.Result::class),
		JSON.ExternalType(TestEntity::class),
		JSON.ExternalType(TestCommandRequestMeta::class),
		JSON.ExternalType(TestCommandResponseMeta::class)
	]
)
internal interface TestJsonCodecProvider : JSONCodecProvider<JSONCodingContext>
