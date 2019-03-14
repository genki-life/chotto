package team.genki.chotto

import com.github.fluidsonic.fluid.json.*


internal object StandardModule : ChottoModule<ChottoContext, ChottoTransaction>() {

	override fun ChottoModuleConfiguration<ChottoContext, ChottoTransaction>.configure() {
		bson(
			CityNameBSONCodec,
			CompanyNameBSONCodec,
			CountryBSONCodec,
			CurrencyBSONCodec,
			DayOfWeekBSONCodec,
			EmailAddressBSONCodec,
			FirstNameBSONCodec,
			FullNameBSONCodec,
			GeoCoordinateBSONCodec,
			LastNameBSONCodec,
			PasswordHashBSONCodec,
			PhoneNumberBSONCodec,
			PostalCodeBSONCodec,
			UrlBSONCodec,
			ZoneIdBSONCodec
		)

		json(
			CountryJSONCodec,
			CurrencyJSONCodec,
			UrlJSONCodec,
			JSONCodecProvider.generated(ChottoJSONCodecProvider::class),
			EnumJSONCodecProvider(transformation = EnumJSONTransformation.ToString(EnumJSONTransformation.Case.`lowercase words`))
		)
	}
}
