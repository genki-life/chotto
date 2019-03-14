import org.gradle.api.artifacts.dsl.DependencyHandler


@Suppress("unused")
fun DependencyHandler.fluid(name: String, version: String) =
	"com.github.fluidsonic:fluid-$name:$version"


@Suppress("unused")
fun DependencyHandler.ktor(name: String, version: String = "1.1.3") =
	"io.ktor:ktor-$name:$version"
