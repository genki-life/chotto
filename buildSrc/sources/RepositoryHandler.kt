import org.gradle.api.artifacts.dsl.*
import org.gradle.api.artifacts.repositories.*
import org.gradle.kotlin.dsl.*


fun RepositoryHandler.bintray(name: String): MavenArtifactRepository =
	maven("https://dl.bintray.com/$name")
