package cinescout.plugins.android

import cinescout.plugins.util.apply
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
internal class AndroidComposeLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target.pluginManager) {
            apply<AndroidLibraryPlugin>()
            apply<AndroidComposePlugin>()
        }
    }
}
