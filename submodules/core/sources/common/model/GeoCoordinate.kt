package team.genki.chotto.core


// TODO make fluid-stdlib multiplatform
expect class GeoCoordinate(
	latitude: Double,
	longitude: Double
) {

	val latitude: Double
	val longitude: Double
}


expect fun GeoCoordinate.distanceTo(coordinate: GeoCoordinate): Double
expect fun GeoCoordinate.distanceTo(latitude: Double, longitude: Double): Double
