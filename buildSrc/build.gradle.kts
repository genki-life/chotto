import org.jetbrains.kotlin.gradle.plugin.*


plugins {
	kotlin("jvm") version "1.3.40-eap-32"
	`kotlin-dsl`
}

repositories {
	bintray("kotlin/kotlin-eap")
	gradlePluginPortal()
	jcenter()
}

dependencies {
	implementation(kotlin("gradle-plugin"))

	api(kotlin("serialization"))
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
