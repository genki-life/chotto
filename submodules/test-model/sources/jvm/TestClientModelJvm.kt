package tests

import team.genki.chotto.client.model.*
import com.github.fluidsonic.fluid.json.*


actual val testJsonConfiguration = JsonConfiguration(
	codecProvider = JSONCodecProvider.generated(TestJsonCodecProvider::class)
)
