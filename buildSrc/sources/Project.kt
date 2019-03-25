import org.gradle.api.Project


val Project.needsWorkaroundForKT30413 // FIXME remove once fixed
	get() = gradle.parent != null
