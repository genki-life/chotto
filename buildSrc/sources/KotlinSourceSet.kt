import org.gradle.api.NamedDomainObjectContainer
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet


inline operator fun KotlinSourceSet.invoke(configure: KotlinSourceSet.() -> Unit) =
	configure()


val NamedDomainObjectContainer<KotlinSourceSet>.jvmMain: KotlinSourceSet
	get() = getByName("jvmMain")


val NamedDomainObjectContainer<KotlinSourceSet>.jvmTest: KotlinSourceSet
	get() = getByName("jvmTest")
