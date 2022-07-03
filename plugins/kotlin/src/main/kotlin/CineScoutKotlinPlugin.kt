import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

abstract class CineScoutKotlinPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.setupKotlinPlugin()
    }
}

private fun Project.setupKotlinPlugin() {
    apply(plugin = "com.google.devtools.ksp")

    if (hasKotlinAndroidPlugin().not()) {
        apply(plugin = "org.jetbrains.kotlin.multiplatform")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlin.RequiresOptIn"
            )
        }
        setSource("build/generated/ksp/main/kotlin")
        setSource("build/generated/ksp/test/kotlin")
    }
}

private fun Project.hasKotlinAndroidPlugin() =
    plugins.hasPlugin("org.jetbrains.kotlin.android")
