import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler


@Suppress("unused")
fun KotlinDependencyHandler.kotlinx(module: String, version: String) =
	"org.jetbrains.kotlinx:kotlinx-$module:$version"


@Suppress("unused")
fun KotlinDependencyHandler.submodule(name: String) =
	project(":chotto-$name")
