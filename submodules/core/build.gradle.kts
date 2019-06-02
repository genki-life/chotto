import org.jetbrains.kotlin.gradle.plugin.mpp.*

plugins {
	kotlin("multiplatform")
	id("kotlinx-serialization")
}

kotlin {
	iosArm64()
	iosX64()
	jvm()

	sourceSets {
		commonMain {
			kotlin.setSrcDirs(listOf("sources/common"))
			resources.setSrcDirs(emptyList<Any>())

			dependencies {
				implementation(kotlinx("serialization-runtime", "0.11.0"))

				api(kotlin("stdlib-common"))
				api(fluid("stdlib", "0.9.17"))
			}
		}

		commonTest {
			kotlin.setSrcDirs(listOf("sources/commonTest"))
			resources.setSrcDirs(emptyList<Any>())

			dependencies {
				implementation(submodule("test-model"))
				implementation(kotlin("test-common"))
				implementation(kotlin("test-annotations-common"))
			}
		}

		jvmMain {
			kotlin.setSrcDirs(listOf("sources/jvm"))
			resources.setSrcDirs(emptyList<Any>())

			dependencies {
				api(kotlin("stdlib-jdk7"))
			}
		}

		jvmTest {
			kotlin.setSrcDirs(listOf("sources/jvmTest"))
			resources.setSrcDirs(emptyList<Any>())

			dependencies {
				implementation(kotlin("test-junit5"))
				implementation("org.junit.jupiter:junit-jupiter-api:5.4.0")

				runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.0")
				runtimeOnly("org.junit.platform:junit-platform-runner:1.4.0")
			}
		}

		val iosMain by creating {
			kotlin.setSrcDirs(listOf("sources/ios"))
			resources.setSrcDirs(emptyList<Any>())

			dependencies {
				implementation(kotlinx("serialization-runtime-native", "0.11.0"))
			}
		}

		val iosTest by creating {
			kotlin.setSrcDirs(listOf("sources/iosTest"))
			resources.setSrcDirs(emptyList<Any>())
		}

		getByName("iosArm64Main") {
			dependsOn(iosMain)
		}

		getByName("iosX64Main") {
			dependsOn(iosMain)
		}

		getByName("iosX64Test") {
			dependsOn(iosTest)
		}
	}
}


val iosTest by tasks.creating<Task> {
	val device = findProperty("iosDevice")?.toString() ?: "iPhone 8"
	dependsOn("linkTestDebugExecutableIosX64")
	group = JavaBasePlugin.VERIFICATION_GROUP

	doLast {
		val binary = kotlin.targets.getByName<KotlinNativeTarget>("iosX64").binaries.getExecutable("test", "DEBUG").outputFile
		exec {
			println("$ xcrun simctl spawn \"$device\" \"${binary.absolutePath}\"")
			commandLine("xcrun", "simctl", "spawn", device, binary.absolutePath)
		}
	}
}

tasks.named("check") {
	dependsOn("iosTest")
}
