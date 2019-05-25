plugins {
	kotlin("multiplatform")
	//kotlin("kapt")
}

kotlin {
	jvm {
		//withJava()
	}


	sourceSets {
		commonMain {
			kotlin.setSrcDirs(listOf("sources/common"))
			resources.setSrcDirs(emptyList())

			dependencies {
				api(submodule("core"))
			}
		}

		jvmMain {
			kotlin.setSrcDirs(listOf("sources/jvm"))
			resources.setSrcDirs(emptyList())
		}
	}
}

dependencies {
	//"kapt"(fluid("json-annotation-processor", "0.9.21"))
}
