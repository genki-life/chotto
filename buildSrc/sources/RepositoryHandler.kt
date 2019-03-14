import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository


fun RepositoryHandler.bintray(name: String): MavenArtifactRepository =
	maven { it.setUrl("https://dl.bintray.com/$name") }
