pluginManagement {
	repositories {
		maven("https://dl.bintray.com/kotlin/kotlin-eap")
		gradlePluginPortal()
		jcenter()
	}
}


fun RepositoryHandler.bintray(name: String) =
	maven("https://dl.bintray.com/$name")
