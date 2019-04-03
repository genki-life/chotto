import org.gradle.api.Project
import org.gradle.api.invocation.Gradle


val Project.workaroundForKT30667
	get() = gradle.root.rootProject.findProperty("workaroundForKT30667") == "true"


private val Gradle.root
	get() = parent ?: this
