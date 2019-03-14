pluginManagement {
	repositories {
		bintray("kotlin/kotlin-eap")
		gradlePluginPortal()
		jcenter()
	}
}

rootProject.name = "chotto"

submodule("client-connector")
submodule("client-model")
submodule("service")
submodule("test-model")

enableFeaturePreview("GRADLE_METADATA")


fun submodule(name: String) {
	val projectName = ":chotto-$name"

	include(projectName)
	project(projectName).projectDir = rootDir.resolve("submodules/$name")
}
