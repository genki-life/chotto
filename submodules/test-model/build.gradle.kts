plugins {
	kotlin("multiplatform")
	id("kotlinx-serialization")
}

kotlin {
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

		jvmMain {
			kotlin.setSrcDirs(listOf("sources/jvm"))
			resources.setSrcDirs(emptyList<Any>())
		}
	}
}
