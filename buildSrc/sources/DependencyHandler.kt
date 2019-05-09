import org.gradle.api.artifacts.dsl.*
import org.gradle.kotlin.dsl.*


@Suppress("unused")
fun DependencyHandler.fluid(name: String, version: String) =
	"com.github.fluidsonic:fluid-$name:$version"


@Suppress("unused")
fun DependencyHandler.ktor(name: String, version: String = "1.1.3") =
	"io.ktor:ktor-$name:$version"


@Suppress("unused")
fun DependencyHandler.submodule(name: String) =
	project(":chotto-$name")
