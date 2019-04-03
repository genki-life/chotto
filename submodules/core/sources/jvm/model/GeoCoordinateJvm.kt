package team.genki.chotto.core


actual typealias GeoCoordinate = com.github.fluidsonic.fluid.stdlib.GeoCoordinate


@Suppress("EXTENSION_SHADOWED_BY_MEMBER") // https://youtrack.jetbrains.com/issue/KT-24845
actual fun GeoCoordinate.distanceTo(coordinate: GeoCoordinate) =
	distanceTo(coordinate)


actual fun GeoCoordinate.distanceTo(latitude: Double, longitude: Double) =
	distanceTo(GeoCoordinate(latitude, longitude)) // TODO improve to avoid unnecessary object creation
