package team.genki.chotto.client.model

import com.github.fluidsonic.fluid.json.*


actual class JsonConfiguration(
	val codecProvider: JSONCodecProvider<JSONCodingContext>
)
