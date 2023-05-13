package cinescout.plugins.modulecatalog

import cinescout.plugins.util.create
import org.gradle.api.Project
import javax.inject.Inject

open class ModuleCatalogExtension @Inject constructor(private val project: Project) {

    @Deprecated(
        "Use correct accessor: implementation, api, testImplementation, androidTestImplementation",
        ReplaceWith("implementation(module)")
    )
    fun add(module: String) {
        val configurations = project.configurations
        val configurationName = when {
            "commonMainImplementation" in configurations.names -> "commonMainImplementation"
            "implementation" in configurations.names -> "implementation"
            else -> error("No configuration found for module $module")
        }
        project.dependencies.add(configurationName, project.project(":cinescout:$module"))
    }

    fun api(module: String) {
        val configurations = project.configurations
        val configurationName = when {
            "commonMainApi" in configurations.names -> "commonMainApi"
            "api" in configurations.names -> "api"
            else -> error("No configuration found for module $module")
        }
        project.dependencies.add(configurationName, project.project(":cinescout:$module"))
    }

    fun implementation(module: String) {
        val configurations = project.configurations
        val configurationName = when {
            "commonMainImplementation" in configurations.names -> "commonMainImplementation"
            "implementation" in configurations.names -> "implementation"
            else -> error("No configuration found for module $module")
        }
        project.dependencies.add(configurationName, project.project(":cinescout:$module"))
    }

    fun testImplementation(module: String) {
        val configurations = project.configurations
        val configurationName = when {
            "commonTestImplementation" in configurations.names -> "commonTestImplementation"
            "testImplementation" in configurations.names -> "testImplementation"
            else -> error("No configuration found for module $module")
        }
        project.dependencies.add(configurationName, project.project(":cinescout:$module"))
    }

    fun androidTestImplementation(module: String) {
        project.dependencies.add("androidTestImplementation", project.project(":cinescout:$module"))
    }

    companion object {

        fun setup(project: Project): ModuleCatalogExtension = project.extensions.create("moduleDependencies")
    }
}
