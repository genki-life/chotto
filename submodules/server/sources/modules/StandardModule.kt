package team.genki.chotto.server


internal object StandardModule : ChottoModule<ChottoServerContext, ChottoTransaction>() {

	override fun ContextConfiguration.configure() {
		bson(
			CountryBsonCodec,
			CurrencyBsonCodec,
			DayOfWeekBsonCodec,
			EmailAddressBsonCodec,
			GeoCoordinateBsonCodec,
			PasswordHashBsonCodec,
			PhoneNumberBsonCodec,
			TimeZoneBsonCodec
		)
	}
}
