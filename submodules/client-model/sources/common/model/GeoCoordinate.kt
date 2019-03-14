package team.genki.chotto.client.model


// FIXME update fluid-stdlib

expect class GeoCoordinate(
	latitude: Double,
	longitude: Double
) {

	val latitude: Double
	val longitude: Double
}


expect fun GeoCoordinate.distanceTo(coordinate: GeoCoordinate): Double
expect fun GeoCoordinate.distanceTo(latitude: Double, longitude: Double): Double
