package team.genki.chotto

import com.github.fluidsonic.fluid.json.*
import com.github.fluidsonic.fluid.stdlib.*


@JSON.CodecProvider(
	externalTypes = [
		JSON.ExternalType(GeoCoordinate::class)
	]
)
internal interface ChottoJSONCodecProvider : JSONCodecProvider<JSONCodingContext>
