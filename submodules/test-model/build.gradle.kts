import org.jetbrains.kotlin.gradle.plugin.mpp.*

plugins {
	kotlin("multiplatform")
	id("kotlinx-serialization")
}

kotlin {
	iosX64()
	jvm()

	sourceSets {
		commonMain {
			kotlin.setSrcDirs(listOf("sources/common"))
			resources.setSrcDirs(emptyList<Any>())

			dependencies {
				api(submodule("core"))

				implementation(kotlinx("serialization-runtime", "0.11.0"))
			}
		}

		getByName("iosX64Main") {
			kotlin.setSrcDirs(listOf("sources/ios"))
			resources.setSrcDirs(emptyList<Any>())

			dependencies {
				implementation(kotlinx("serialization-runtime-native", "0.11.0"))
			}
		}

		jvmMain {
			kotlin.setSrcDirs(listOf("sources/jvm"))
			resources.setSrcDirs(emptyList<Any>())
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
