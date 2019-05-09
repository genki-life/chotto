import org.gradle.api.*
import org.jetbrains.kotlin.gradle.plugin.*


inline operator fun KotlinSourceSet.invoke(configure: KotlinSourceSet.() -> Unit) =
	configure()


val NamedDomainObjectContainer<KotlinSourceSet>.jvmMain: KotlinSourceSet
	get() = getByName("jvmMain")


val NamedDomainObjectContainer<KotlinSourceSet>.jvmTest: KotlinSourceSet
	get() = getByName("jvmTest")
