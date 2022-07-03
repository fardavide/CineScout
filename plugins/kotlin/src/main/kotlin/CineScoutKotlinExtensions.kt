import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

fun Project.getMultiplatformExtension() =
    extensions.getByType(KotlinMultiplatformExtension::class)

fun Project.findMultiplatformExtension() =
    extensions.findByType<KotlinMultiplatformExtension>()

