plugins {
	kotlin("multiplatform")
}

kotlin {
	sourceSets {
		jvm()

		commonMain {
			kotlin.setSrcDirs(listOf("sources/common"))
			resources.setSrcDirs(emptyList())

			dependencies {
				api(submodule("core"))

				api(kotlin("stdlib-common"))
				api(ktor("client-core"))
			}
		}

		commonTest {
			kotlin.setSrcDirs(listOf("sources/commonTest"))
			resources.setSrcDirs(emptyList())

			dependencies {
				implementation(submodule("test-model"))

				implementation(kotlin("test-common"))
				implementation(kotlin("test-annotations-common"))
				implementation(ktor("client-mock"))
			}
		}

		jvmMain {
			kotlin.setSrcDirs(listOf("sources/jvmMain"))
			resources.setSrcDirs(emptyList())

			dependencies {
				api(kotlin("stdlib-jdk8"))
				api(ktor("client-core-jvm"))
			}
		}

		jvmTest {
			kotlin.setSrcDirs(listOf("sources/jvmTest"))
			resources.setSrcDirs(emptyList())

			dependencies {
				implementation(kotlin("test-junit5"))
				implementation(ktor("client-mock-jvm"))
				implementation("org.junit.jupiter:junit-jupiter-api:5.4.0")

				runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.0")
				runtimeOnly("org.junit.platform:junit-platform-runner:1.4.0")
			}
		}
	}
}
