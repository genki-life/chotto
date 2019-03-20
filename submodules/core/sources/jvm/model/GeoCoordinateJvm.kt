package team.genki.chotto.core


actual typealias GeoCoordinate = com.github.fluidsonic.fluid.stdlib.GeoCoordinate


actual fun GeoCoordinate.distanceTo(coordinate: GeoCoordinate) =
	distanceTo(coordinate)


actual fun GeoCoordinate.distanceTo(latitude: Double, longitude: Double) =
	distanceTo(GeoCoordinate(latitude, longitude)) // TODO improve
