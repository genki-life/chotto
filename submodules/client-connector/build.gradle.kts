import org.gradle.jvm.tasks.Jar


plugins {
	kotlin("multiplatform")
}

kotlin {
	sourceSets {
		targetFromPreset(presets.getByName("jvmWithJava"), "jvm") {
			val javadocJar by tasks.creating(Jar::class) {
				archiveClassifier.set("javadoc")
				from(tasks["javadoc"])
			}

			mavenPublication {
				artifact(javadocJar)
			}
		}

		commonMain {
			kotlin.setSrcDirs(listOf("sources/common"))
			resources.setSrcDirs(emptyList())

			dependencies {
				api(submodule("client-model"))

				api(kotlin("stdlib-common"))
				api("io.ktor:ktor-client-core:1.1.3")
			}
		}

		commonTest {
			kotlin.setSrcDirs(listOf("sources/commonTest"))
			resources.setSrcDirs(emptyList())

			dependencies {
				implementation(submodule("test-model"))

				implementation(kotlin("test-common"))
				implementation(kotlin("test-annotations-common"))
				implementation("io.ktor:ktor-client-mock:1.1.3")
			}
		}

		jvmMain {
			kotlin.setSrcDirs(listOf("sources/jvmMain"))
			resources.setSrcDirs(emptyList())

			dependencies {
				api(kotlin("stdlib-jdk8"))
				api("io.ktor:ktor-client-core-jvm:1.1.3")
			}
		}

		jvmTest {
			kotlin.setSrcDirs(listOf("sources/jvmTest"))
			resources.setSrcDirs(emptyList())

			dependencies {
				implementation(kotlin("test-junit5"))
				implementation("io.ktor:ktor-client-mock-jvm:1.1.3")
				implementation("org.junit.jupiter:junit-jupiter-api:5.4.0")

				runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.0")
				runtimeOnly("org.junit.platform:junit-platform-runner:1.4.0")
			}
		}
	}
}
