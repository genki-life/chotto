pluginManagement {
	repositories {
		maven("https://dl.bintray.com/kotlin/kotlin-eap")
		gradlePluginPortal()
		jcenter()
	}
}

rootProject.name = "chotto"

submodule("client")
submodule("core")
submodule("server")
submodule("test-model")


fun submodule(name: String) {
	val projectName = ":chotto-$name"

	include(projectName)
	project(projectName).projectDir = rootDir.resolve("submodules/$name")
}
