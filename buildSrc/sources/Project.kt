import com.github.benmanes.gradle.versions.updates.*
import org.gradle.api.*
import org.gradle.kotlin.dsl.*


fun Project.dependencyUpdates(configuration: DependencyUpdatesTask.() -> Unit) =
	tasks.withType(configuration)


fun isUnstableVersion(version: String) =
	Regex("\\b(alpha|beta|eap|rc|snapshot)\\d*\\b", RegexOption.IGNORE_CASE).containsMatchIn(version)
