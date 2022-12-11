import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

abstract class CineScoutKotlinPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.setupKotlinPlugin()
    }
}

private fun Project.setupKotlinPlugin() {
    if (hasKotlinAndroidPlugin().not()) {
        apply(plugin = "org.jetbrains.kotlin.multiplatform")
    }
    apply(plugin = "com.google.devtools.ksp")

    setupOptIns(
        "androidx.compose.material3.ExperimentalMaterial3Api",
        "androidx.compose.ui.test.ExperimentalTestApi",
        "kotlin.experimental.ExperimentalTypeInference",
        "kotlin.time.ExperimentalTime",
        "kotlinx.coroutines.ExperimentalCoroutinesApi",
        "kotlinx.coroutines.FlowPreview"
    )
    enableJvmContextReceivers()
    setupKsp()
}

private fun Project.hasKotlinAndroidPlugin() =
    plugins.hasPlugin("org.jetbrains.kotlin.android")
