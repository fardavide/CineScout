package cinescout.plugins.android

import cinescout.plugins.util.apply
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
internal class AndroidLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.pluginManager.apply("com.android.library")
        target.pluginManager.apply<AndroidPlugin>()
    }
}
