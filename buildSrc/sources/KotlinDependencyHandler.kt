import org.jetbrains.kotlin.gradle.plugin.*


@Suppress("unused")
fun KotlinDependencyHandler.fluid(name: String, version: String) =
	"com.github.fluidsonic:fluid-$name:$version"


@Suppress("unused")
fun KotlinDependencyHandler.kotlinx(module: String, version: String) =
	"org.jetbrains.kotlinx:kotlinx-$module:$version"


@Suppress("unused")
fun KotlinDependencyHandler.ktor(name: String, version: String = "1.2.1") =
	"io.ktor:ktor-$name:$version"


@Suppress("unused")
fun KotlinDependencyHandler.submodule(name: String) =
	project(":chotto-$name")
