import org.gradle.jvm.tasks.Jar

plugins {
	kotlin("multiplatform")
	kotlin("kapt")
}

kotlin {
	targetFromPreset(presets.getByName("jvmWithJava"), "jvm") {
		val javadocJar by tasks.creating(Jar::class) {
			archiveClassifier.set("javadoc")
			from(tasks["javadoc"])
		}

		tasks.getByName<Jar>("jvmSourcesJar") {
			from(file("build/generated/source/kaptKotlin/main"))
		}

		mavenPublication {
			artifact(javadocJar)
		}
	}

	sourceSets {
		commonMain {
			kotlin.setSrcDirs(listOf("sources/common"))
			resources.setSrcDirs(emptyList())

			dependencies {
				api(kotlin("stdlib-common"))
			}
		}

		commonTest {
			kotlin.setSrcDirs(listOf("sources/commonTest"))
			resources.setSrcDirs(emptyList())

			dependencies {
				implementation(submodule("test-model"))

				implementation(kotlin("test-common"))
				implementation(kotlin("test-annotations-common"))
			}
		}

		jvmMain {
			kotlin.setSrcDirs(listOf("sources/jvm"))
			resources.setSrcDirs(emptyList())

			dependencies {
				api(kotlin("stdlib-jdk8"))

				api("com.github.fluidsonic:fluid-json-coding-jdk8:0.9.17")
				api("com.github.fluidsonic:fluid-stdlib-jdk8:0.9.1")

				compileOnly("com.github.fluidsonic:fluid-json-annotations:0.9.17")
			}
		}

		jvmTest {
			kotlin.setSrcDirs(listOf("sources/jvmTest"))
			resources.setSrcDirs(emptyList())

			dependencies {
				implementation(kotlin("test-junit5"))
				implementation("org.junit.jupiter:junit-jupiter-api:5.4.0")

				runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.0")
				runtimeOnly("org.junit.platform:junit-platform-runner:1.4.0")
			}
		}
	}
}

dependencies {
	"kapt"("com.github.fluidsonic:fluid-json-annotation-processor:0.9.17")
}
