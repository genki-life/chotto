package tests

import com.github.fluidsonic.fluid.json.*
import team.genki.chotto.core.*


actual val testJsonConfiguration = JsonConfiguration(
	codecProvider = JSONCodecProvider.generated(TestJsonCodecProvider::class)
)
