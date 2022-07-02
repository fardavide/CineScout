import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

val Project.multiplatformExtension get() =
    extensions.findByType<KotlinMultiplatformExtension>()

