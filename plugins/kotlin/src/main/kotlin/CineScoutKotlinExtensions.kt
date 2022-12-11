import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile as KotlinCompileTask

val Project.sourceSets: SourceSetContainer
    get() = (this as ExtensionAware).extensions.getByName("sourceSets") as SourceSetContainer

fun Project.getMultiplatformExtension() =
    extensions.getByType(KotlinMultiplatformExtension::class)

fun Project.findMultiplatformExtension() =
    extensions.findByType<KotlinMultiplatformExtension>()


internal fun Project.setupKsp() {
    sourceSets.configureEach {
        allSource.srcDirs(
            "build/generated/ksp/${this.name}/kotlin"
        )
    }
    findMultiplatformExtension()?.apply {
        sourceSets.all {
            kotlin.srcDir("build/generated/ksp/jvm/jvmMain/kotlin")
        }
    }
}
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
                val optInArgs = optIns.map { "-opt-in=$it" }
                freeCompilerArgs = freeCompilerArgs + optInArgs
            }
        }
    }
}

internal fun Project.enableJvmContextReceivers() {
    tasks.withType<KotlinCompileTask> {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers"
        }
    }
}
