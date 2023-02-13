package cinescout.plugins.modulescatalog

import org.gradle.api.Project
import shuttle.plugins.util.create
import javax.inject.Inject

open class ModulesCatalogExtension @Inject constructor(private val project: Project) {

    fun add(module: String) {
        val configurations = project.configurations
        val configurationName = when {
            "implementation" in configurations.names -> "implementation"
            "commonMainImplementation" in configurations.names -> "commonMainImplementation"
            else -> error("No configuration found for module $module")
        }
        project.dependencies.add(configurationName, project.project(":cinescout:$module"))
    }

    companion object {

        fun setup(project: Project): ModulesCatalogExtension = project.extensions.create("moduleDependencies")
    }
}
