package cinescout.plugins.detekt

import cinescout.plugins.util.apply
import cinescout.plugins.util.libsCatalog
import cinescout.plugins.util.withType
import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.dsl.DependencyHandler

@Suppress("unused")
internal class DetektPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target.pluginManager) {
            apply<io.gitlab.arturbosch.detekt.DetektPlugin>()
            apply("io.github.detekt.gradle.compiler-plugin")
        }

        target.tasks.withType<Detekt> { task ->
            task.autoCorrect = true
            task.config.setFrom("${target.rootDir.path}/detekt/config.yml")
        }

        val catalog = target.libsCatalog
        target.dependencies.apply {
            addDetektPlugin(catalog, plugin = "detekt-rules-compose")
            addDetektPlugin(catalog, plugin = "detekt-rules-formatting")
            addDetektPlugin(target.rootProject.project(":detekt:rules"))
        }
    }

    private fun DependencyHandler.addDetektPlugin(catalog: VersionCatalog, plugin: String) {
        add("detektPlugins", catalog.findLibrary(plugin).get())
    }

    private fun DependencyHandler.addDetektPlugin(plugin: Project) {
        add("detektPlugins", plugin)
    }
}
