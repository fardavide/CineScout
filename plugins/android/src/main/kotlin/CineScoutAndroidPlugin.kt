
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

@Suppress("unused")
abstract class CineScoutAndroidPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        CineScoutAndroidExtension.setup(target)
        target.setupAndroidPlugin()
    }
}

private fun Project.setupAndroidPlugin() {
    apply(plugin = "org.jetbrains.kotlin.android")
    if (isApp()) {
        apply(plugin = "com.android.application")
        // TODO apply(plugin = "com.google.gms.google-services")
        // TODO apply(plugin = "com.google.firebase.crashlytics")
    } else {
        apply(plugin = "com.android.library")
    }
    apply<CineScoutKotlinPlugin>()

    configureTestedExtension()
}

private fun Project.isApp() = "app" in name
