import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet


plugins {
	kotlin("jvm") version "1.3.30-eap-11"
}

repositories {
	bintray("kotlin/kotlin-eap")
	gradlePluginPortal()
	jcenter()
}

dependencies {
	implementation(kotlin("gradle-plugin"))
}

sourceSets {
	getByName("main") {
		kotlin.srcDirs("sources")
	}
}


fun RepositoryHandler.bintray(name: String) =
	maven("https://dl.bintray.com/$name")


val SourceSet.kotlin
	get() = withConvention(KotlinSourceSet::class) { kotlin }
