import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.kotlin.dsl.*


fun RepositoryHandler.bintray(name: String): MavenArtifactRepository =
	maven("https://dl.bintray.com/$name")
