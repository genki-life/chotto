plugins {
	kotlin("multiplatform")
	kotlin("kapt")
}

kotlin {
	targetFromPreset(presets.getByName("jvmWithJava"), "jvm")

	sourceSets {
		commonMain {
			kotlin.setSrcDirs(listOf("sources/common"))
			resources.setSrcDirs(emptyList())

			dependencies {
				api(submodule("client-model"))
			}
		}

		jvmMain {
			kotlin.setSrcDirs(listOf("sources/jvm"))
			resources.setSrcDirs(emptyList())
		}
	}
}

dependencies {
	"kapt"("com.github.fluidsonic:fluid-json-annotation-processor:0.9.17")
}
