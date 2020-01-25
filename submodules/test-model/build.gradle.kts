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
				implementation(kotlinx("serialization-runtime-common", "0.14.0"))

				api(submodule("core"))
			}
		}

		jvmMain {
			kotlin.setSrcDirs(listOf("sources/jvm"))
			resources.setSrcDirs(emptyList<Any>())

			dependencies {
				implementation(kotlinx("serialization-runtime", "0.14.0"))
			}
		}

		val iosMain by creating {
			kotlin.setSrcDirs(listOf("sources/ios"))
			resources.setSrcDirs(emptyList<Any>())
		}

		getByName("iosArm64Main") {
			dependsOn(iosMain)

			dependencies {
				implementation(kotlinx("serialization-runtime-iosarm64", "0.14.0"))
			}
		}

		getByName("iosX64Main") {
			dependsOn(iosMain)

			dependencies {
				implementation(kotlinx("serialization-runtime-iosx64", "0.14.0"))
			}
		}
	}
}
