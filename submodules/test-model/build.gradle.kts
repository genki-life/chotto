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

				api(submodule("core"))
			}
		}

		jvmMain {
			kotlin.setSrcDirs(listOf("sources/jvm"))
			resources.setSrcDirs(emptyList<Any>())
		}

		val iosMain by creating {
			kotlin.setSrcDirs(listOf("sources/ios"))
			resources.setSrcDirs(emptyList<Any>())

			dependencies {
				implementation(kotlinx("serialization-runtime-native", "0.11.0"))
			}
		}

		getByName("iosArm64Main") {
			dependsOn(iosMain)
		}

		getByName("iosX64Main") {
			dependsOn(iosMain)
		}
	}
}
