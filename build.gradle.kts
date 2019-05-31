import com.jfrog.bintray.gradle.*
import com.jfrog.bintray.gradle.tasks.*
import org.gradle.api.publish.maven.internal.artifact.*
import org.gradle.api.tasks.testing.logging.*

plugins {
	`junit-test-suite`
	publishing

	id("com.github.ben-manes.versions") version "0.21.0"
	id("com.jfrog.bintray") version "1.8.4" apply false
}

val subprojectsForPublishing = setOf(
	"chotto-client",
	"chotto-core",
	"chotto-server"
)

subprojects {
	group = "team.genki"
	version = "0.9.20"

	apply<JUnitTestSuitePlugin>()

	repositories {
		bintray("fluidsonic/maven")
		bintray("genki/maven")
		bintray("kotlin/kotlin-eap")
		bintray("kotlin/kotlinx")
		jcenter()
		mavenCentral()
	}

	tasks.withType<Test> {
		useJUnitPlatform {
			includeEngines("junit-jupiter")
		}

		reports {
			html.isEnabled = false
			junitXml.isEnabled = false
		}

		testLogging {
			events(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)

			exceptionFormat = TestExceptionFormat.FULL
			showExceptions = true
			testLogging.showStandardStreams = true
		}
	}

	if (subprojectsForPublishing.contains(name)) {
		apply<MavenPublishPlugin>() // FIXME remove once KT-30413 is fixed

		val bintrayUser = findProperty("bintrayUser") as String?
		val bintrayKey = findProperty("bintrayApiKey") as String?
		if (bintrayUser != null && bintrayKey != null) {
			apply<BintrayPlugin>()
			apply<PublishingPlugin>()

			configure<BintrayExtension> {
				user = bintrayUser
				key = bintrayKey

				pkg.apply {
					repo = "maven"
					issueTrackerUrl = "https://github.com/genki/chotto/issues"
					name = project.name
					publicDownloadNumbers = true
					publish = true
					userOrg = "genki"
					vcsUrl = "https://github.com/genki/chotto"
					websiteUrl = "https://github.com/genki/chotto"
					setLicenses("Apache-2.0")

					version.apply {
						name = project.version.toString()
						vcsTag = project.version.toString()
					}
				}

				afterEvaluate {
					setPublications(*publishing.publications.names.toTypedArray())
				}
			}

			tasks.withType<BintrayUploadTask> {
				doFirst {
					publishing.publications
						.filterIsInstance<MavenPublication>()
						.forEach { publication ->
							val moduleFile = buildDir.resolve("publications/${publication.name}/module.json")
							if (moduleFile.exists()) {
								publication.artifact(object : FileBasedMavenArtifact(moduleFile) {
									override fun getDefaultExtension() = "module"
								})
							}
						}
				}
			}
		}
	}
}

tasks.withType<Wrapper> {
	distributionType = Wrapper.DistributionType.ALL
	gradleVersion = "5.4.1"
}
