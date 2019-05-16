package team.genki.chotto.core

import com.github.fluidsonic.fluid.json.*
import com.github.fluidsonic.fluid.stdlib.*


// note https://youtrack.jetbrains.com/issue/KT-27598
@JSON.CodecProvider(
	externalTypes = [
		// model
		JSON.ExternalType(AccessToken::class, JSON(representation = JSON.Representation.singleValue)),
		JSON.ExternalType(Cents::class, JSON(representation = JSON.Representation.singleValue), targetName = "com.github.fluidsonic.fluid.stdlib.Cents"),
		JSON.ExternalType(Change::class, JSON(representation = JSON.Representation.singleValue)),
		JSON.ExternalType(EmailAddress::class, JSON(representation = JSON.Representation.singleValue)),
		JSON.ExternalType(Formatted::class),
		JSON.ExternalType(GeoCoordinate::class),
		JSON.ExternalType(Money::class),
		JSON.ExternalType(Password::class, JSON(representation = JSON.Representation.singleValue)),
		JSON.ExternalType(PhoneNumber::class, JSON(representation = JSON.Representation.singleValue)),
		JSON.ExternalType(RefreshToken::class, JSON(representation = JSON.Representation.singleValue))
	]
)
internal interface GeneratedJsonCodecProvider : JSONCodecProvider<JSONCodingContext>
