import org.jetbrains.kotlin.gradle.plugin.mpp.*

plugins {
	kotlin("multiplatform")
	`maven-publish`
}

kotlin {
	iosArm64()
	iosX64()

	jvm {
		compilations {
			all {
				if (name.contains("test")) {
					kotlinOptions.jvmTarget = "1.8"
					compileKotlinTask.sourceCompatibility = "1.8"
					compileKotlinTask.targetCompatibility = "1.8"

					attributes {
						attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 8)
					}
				}
				else {
					kotlinOptions.jvmTarget = "1.6"
					compileKotlinTask.sourceCompatibility = "1.7"
					compileKotlinTask.targetCompatibility = "1.7"

					attributes {
						attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 7)
					}
				}
			}
		}
	}

	sourceSets {
		commonMain {
			kotlin.setSrcDirs(listOf("sources/common"))
			resources.setSrcDirs(emptyList<Any>())

			dependencies {
				implementation(kotlinx("serialization-runtime-common", "0.14.0"))

				api(submodule("core"))
				api(kotlin("stdlib-common"))
				api(ktor("client-core"))
			}
		}

		commonTest {
			kotlin.setSrcDirs(listOf("sources/commonTest"))
			resources.setSrcDirs(emptyList<Any>())

			dependencies {
				implementation(submodule("test-model"))
				implementation(kotlin("test-common"))
				implementation(kotlin("test-annotations-common"))
				implementation(ktor("client-mock"))
			}
		}

		jvmMain {
			kotlin.setSrcDirs(listOf("sources/jvm"))
			resources.setSrcDirs(emptyList<Any>())

			dependencies {
				implementation(kotlinx("serialization-runtime", "0.14.0"))

				api(kotlin("stdlib-jdk8"))
				api(ktor("client-apache")) // TODO add different targets for JVM and Android
				api(ktor("client-core-jvm"))
			}
		}

		jvmTest {
			kotlin.setSrcDirs(listOf("sources/jvmTest"))
			resources.setSrcDirs(emptyList<Any>())

			dependencies {
				implementation(kotlin("test-junit5"))
				implementation(ktor("client-mock-jvm"))
				implementation("org.junit.jupiter:junit-jupiter-api:5.6.0")

				runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
				runtimeOnly("org.junit.platform:junit-platform-runner:1.6.0")
			}
		}

		val iosMain by creating {
			kotlin.setSrcDirs(emptyList<Any>())
			resources.setSrcDirs(emptyList<Any>())

			dependencies {
				api(ktor("client-ios"))
			}
		}

		val iosTest by creating {
			kotlin.setSrcDirs(emptyList<Any>())
			resources.setSrcDirs(emptyList<Any>())
		}

		getByName("iosArm64Main") {
			kotlin.setSrcDirs(listOf("sources/iosArm64"))
			resources.setSrcDirs(emptyList<Any>())

			dependsOn(iosMain)

			dependencies {
				implementation(kotlinx("serialization-runtime-iosarm64", "0.14.0"))

				api(ktor("client-core-iosarm64"))
			}
		}

		getByName("iosArm64Test") {
			kotlin.setSrcDirs(listOf("sources/iosArm64Test"))
			resources.setSrcDirs(emptyList<Any>())

			dependsOn(iosTest)

			dependencies {
				implementation(ktor("client-mock-iosarm64"))
			}
		}

		getByName("iosX64Main") {
			kotlin.setSrcDirs(listOf("sources/iosX64"))
			resources.setSrcDirs(emptyList<Any>())

			dependsOn(iosMain)

			dependencies {
				implementation(kotlinx("serialization-runtime-iosx64", "0.14.0"))

				api(ktor("client-core-iosx64"))
			}
		}

		getByName("iosX64Test") {
			kotlin.setSrcDirs(listOf("sources/iosX64Test"))
			resources.setSrcDirs(emptyList<Any>())

			dependsOn(iosTest)

			dependencies {
				implementation(ktor("client-mock-iosx64"))
			}
		}
	}
}


val iosTest by tasks.creating<Task> {
	val device = findProperty("iosDevice")?.toString() ?: "iPhone 11"

	val iosTarget = kotlin.targets.getByName("iosX64") as KotlinNativeTarget
	val binary = iosTarget.binaries.getTest(NativeBuildType.DEBUG)
	val binaryFile = binary.outputFile

	dependsOn(binary.linkTask)

	group = JavaBasePlugin.VERIFICATION_GROUP

	doLast {
		exec {
			println("$ xcrun simctl spawn --standalone \"$device\" \"${binaryFile.absolutePath}\"")
			commandLine("xcrun", "simctl", "spawn", "--standalone", device, binaryFile.absolutePath)
		}
	}
}

tasks.named("check") {
	dependsOn("iosTest")
}
