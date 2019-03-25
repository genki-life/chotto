import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
}

dependencies {
	if (needsWorkaroundForKT30413) api("team.genki:chotto-core-jvm:$version")
	else api(submodule("core"))

	api(fluid("mongo", "0.9.4"))
	api(fluid("stdlib-jdk8", "0.9.1"))
	api(kotlin("stdlib-jdk8"))
	api(ktor("auth-jwt"))
	api(ktor("server-netty"))

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
