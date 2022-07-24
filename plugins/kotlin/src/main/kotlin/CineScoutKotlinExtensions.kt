import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun Project.getMultiplatformExtension() =
    extensions.getByType(KotlinMultiplatformExtension::class)

fun Project.findMultiplatformExtension() =
    extensions.findByType<KotlinMultiplatformExtension>()

internal fun Project.setupOptIns(vararg optIns: String) {
    val multiplatformExtension = findMultiplatformExtension()

    if (multiplatformExtension != null) {
        multiplatformExtension.sourceSets.all {
            for (annotationName in optIns) {
                languageSettings.optIn(annotationName)
            }
        }
    } else {
        tasks.withType<KotlinCompile> {
            kotlinOptions {
                val optInArgs = optIns.map { "-Xopt-in=$it" }
                freeCompilerArgs = freeCompilerArgs + optInArgs
            }
        }
    }
}

