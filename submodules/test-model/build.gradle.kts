plugins {
	kotlin("multiplatform")
	kotlin("kapt")
}

kotlin {
	if (workaroundForKT30667) jvm()
	else targetFromPreset(presets.getByName("jvmWithJava"), "jvm")


	sourceSets {
		commonMain {
			kotlin.setSrcDirs(listOf("sources/common"))
			resources.setSrcDirs(emptyList())

			dependencies {
				api(submodule("core"))
			}
		}

		jvmMain {
			kotlin.setSrcDirs(
				if (workaroundForKT30667) listOf("sources/jvm", "build/generated/source/kaptKotlin/main")
				else listOf("sources/jvm")
			)
			resources.setSrcDirs(emptyList())
		}
	}
}

dependencies {
	"kapt"(fluid("json-annotation-processor", "0.9.21"))
}
