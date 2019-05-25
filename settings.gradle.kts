pluginManagement {
	repositories {
		bintray("kotlin/kotlin-eap")
		gradlePluginPortal()
		jcenter()
	}
}

rootProject.name = "chotto"

submodule("client")
submodule("core")
submodule("server")
submodule("test-model")

enableFeaturePreview("GRADLE_METADATA")


//if (System.getProperty("user.name") == "marc") {
//	includeBuild("../fluid-library")
//	includeBuild("../fluid-stdlib")
//	includeBuild("../fluid-time")
//}


fun submodule(name: String) {
	val projectName = ":chotto-$name"

	include(projectName)
	project(projectName).projectDir = rootDir.resolve("submodules/$name")
}
