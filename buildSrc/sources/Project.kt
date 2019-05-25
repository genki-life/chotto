import org.gradle.api.*
import org.gradle.api.invocation.*


val Project.workaroundForKT30667 // FIXME remove
	get() = false// gradle.root.rootProject.findProperty("workaroundForKT30667") == "true"


private val Gradle.root
	get() = parent ?: this
