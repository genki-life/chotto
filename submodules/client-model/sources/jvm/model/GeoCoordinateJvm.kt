@file:Suppress("unused")

package team.genki.chotto.client.model


actual typealias GeoCoordinate = com.github.fluidsonic.fluid.stdlib.GeoCoordinate


actual fun GeoCoordinate.distanceTo(coordinate: GeoCoordinate) =
	distanceTo(coordinate)


actual fun GeoCoordinate.distanceTo(latitude: Double, longitude: Double) =
	distanceTo(team.genki.chotto.client.model.GeoCoordinate(latitude, longitude)) // TODO improve
