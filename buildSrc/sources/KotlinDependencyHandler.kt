import org.jetbrains.kotlin.gradle.plugin.*


@Suppress("unused")
fun KotlinDependencyHandler.fluid(name: String, version: String) =
	"io.fluidsonic.${name.substringBefore('-')}:fluid-$name:$version"


@Suppress("unused")
fun KotlinDependencyHandler.kotlinx(module: String, version: String) =
	"org.jetbrains.kotlinx:kotlinx-$module:$version"


@Suppress("unused")
fun KotlinDependencyHandler.ktor(name: String, version: String = "1.3.0") =
	"io.ktor:ktor-$name:$version"


@Suppress("unused")
fun KotlinDependencyHandler.submodule(name: String) =
	project(":chotto-$name")
