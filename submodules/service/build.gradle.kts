plugins {
	kotlin("jvm")
}


dependencies {
	api(fluid("mongo", "0.9.4"))
	api(fluid("stdlib-jdk8", "0.9.1"))

	api(ktor("auth-jwt"))
	api(ktor("server-netty"))

	implementation("ch.qos.logback:logback-classic:1.2.1")
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
