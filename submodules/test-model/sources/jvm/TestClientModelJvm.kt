package tests

import com.github.fluidsonic.fluid.json.*
import team.genki.chotto.core.*


actual val testJsonConfiguration = JsonConfiguration(
	modelCodecProvider = JSONCodecProvider.generated(TestJsonCodecProvider::class)
)
