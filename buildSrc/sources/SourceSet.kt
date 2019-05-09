import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.plugin.*


val SourceSet.kotlin
	get() = withConvention(KotlinSourceSet::class) { kotlin }
