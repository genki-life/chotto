package team.genki.chotto.core

import com.github.fluidsonic.fluid.json.*


actual class JsonConfiguration(
	val modelCodecProvider: JSONCodecProvider<JSONCodingContext>
)
