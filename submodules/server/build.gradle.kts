import org.jetbrains.kotlin.gradle.tasks.*

plugins {
	kotlin("jvm")
}

dependencies {
	api(submodule("core"))

	api(fluid("mongo", "0.9.5"))
	api(kotlin("stdlib-jdk8"))
	api(ktor("auth-jwt"))
	api(ktor("server-netty"))

	implementation(kotlinx("serialization-runtime", "0.11.0"))
	implementation("ch.qos.logback:logback-classic:1.2.1")
}

java {
	sourceCompatibility = JavaVersion.VERSION_1_8
	targetCompatibility = JavaVersion.VERSION_1_8
}

sourceSets {
	getByName("main") {
		kotlin.setSrcDirs(listOf("sources"))
		resources.setSrcDirs(listOf("resources"))
	}

	getByName("test") {
		kotlin.setSrcDirs(listOf("tests/sources"))
		resources.setSrcDirs(listOf("tests/resources"))
	}
}

tasks.withType<KotlinCompile> {
	sourceCompatibility = "1.8"
	targetCompatibility = "1.8"

	kotlinOptions.jvmTarget = "1.8"
}

val javadocJar by tasks.creating(Jar::class) {
	archiveClassifier.set("javadoc")
	from(tasks["javadoc"])
}

val sourcesJar by tasks.creating(Jar::class) {
	archiveClassifier.set("sources")
	from(sourceSets["main"].allSource)
}

artifacts {
	archives(javadocJar)
	archives(sourcesJar)
}

publishing {
	publications {
		create<MavenPublication>("default") {
			from(components["java"])
		}
	}
}
