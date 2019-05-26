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
				implementation(kotlinx("serialization-runtime", "0.11.0"))

				api(submodule("core"))
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
