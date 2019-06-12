import org.gradle.api.artifacts.dsl.*
import org.gradle.kotlin.dsl.*


@Suppress("unused")
fun DependencyHandler.fluid(name: String, version: String) =
	"com.github.fluidsonic:fluid-$name:$version"


@Suppress("unused")
fun DependencyHandler.kotlinx(module: String, version: String) =
	"org.jetbrains.kotlinx:kotlinx-$module:$version"


@Suppress("unused")
fun DependencyHandler.ktor(name: String, version: String = "1.2.1") =
	"io.ktor:ktor-$name:$version"


@Suppress("unused")
fun DependencyHandler.submodule(name: String) =
	project(":chotto-$name")
