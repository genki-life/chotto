plugins {
	kotlin("multiplatform")
	id("kotlinx-serialization")
}

kotlin {
	jvm()

	sourceSets {
		commonMain {
			kotlin.setSrcDirs(listOf("sources/common"))
			resources.setSrcDirs(emptyList())

			dependencies {
				api(kotlin("stdlib-common"))
				api(fluid("stdlib", "0.9.9"))

				implementation(kotlinx("serialization-runtime", "0.11.0"))
			}
		}

		commonTest {
			kotlin.setSrcDirs(listOf("sources/commonTest"))
			resources.setSrcDirs(emptyList())

			dependencies {
				implementation(submodule("test-model"))
				implementation(kotlin("test-common"))
				implementation(kotlin("test-annotations-common"))
			}
		}

		jvmMain {
			kotlin.setSrcDirs(listOf("sources/jvm"))
			resources.setSrcDirs(emptyList())

			dependencies {
				api(kotlin("stdlib-jdk8")) // FIXME Android
				api(fluid("json-coding-jdk8", "0.9.21")) // FIXME Android

				compileOnly(fluid("json-annotations", "0.9.21"))
			}
		}

		jvmTest {
			kotlin.setSrcDirs(listOf("sources/jvmTest"))
			resources.setSrcDirs(emptyList())

			dependencies {
				implementation(kotlin("test-junit5"))
				implementation("org.junit.jupiter:junit-jupiter-api:5.4.0")

				runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.0")
				runtimeOnly("org.junit.platform:junit-platform-runner:1.4.0")
			}
		}
	}
}
