package cinescout.plugins.modulecatalog

import org.gradle.api.Project
import cinescout.plugins.util.create
import javax.inject.Inject

open class ModuleCatalogExtension @Inject constructor(private val project: Project) {

    @Deprecated(
        "Use correct accessor: implementation, api, testImplementation, androidTestImplementation",
        ReplaceWith("implementation(module)")
    )
    fun add(module: String) {
        val configurations = project.configurations
        val configurationName = when {
            "implementation" in configurations.names -> "implementation"
            "commonMainImplementation" in configurations.names -> "commonMainImplementation"
            else -> error("No configuration found for module $module")
        }
        project.dependencies.add(configurationName, project.project(":cinescout:$module"))
    }
    
    private fun Project.allProjects(): List<Project> =
        childProjects.values
            .flatMap { project ->
                listOf(project) + project.allProjects()
            }

    fun api(module: String) {
        val configurations = project.configurations
        val configurationName = when {
            "api" in configurations.names -> "api"
            "commonMainApi" in configurations.names -> "commonMainApi"
            else -> error("No configuration found for module $module")
        }
        project.dependencies.add(configurationName, project.project(":cinescout:$module"))
    }

    fun implementation(module: String) {
        val configurations = project.configurations
        val configurationName = when {
            "implementation" in configurations.names -> "implementation"
            "commonMainImplementation" in configurations.names -> "commonMainImplementation"
            else -> error("No configuration found for module $module")
        }
        project.dependencies.add(configurationName, project.project(":cinescout:$module"))
    }

    fun testImplementation(module: String) {
        val configurations = project.configurations
        val configurationName = when {
            "testImplementation" in configurations.names -> "testImplementation"
            "commonTestImplementation" in configurations.names -> "commonTestImplementation"
            else -> error("No configuration found for module $module")
        }
        project.dependencies.add(configurationName, project.project(":cinescout:$module"))
    }

    fun androidTestImplementation(module: String) {
        project.dependencies.add("androidTestImplementation", project.project(":cinescout:$module"))
    }

    companion object {

        fun setup(project: Project): ModuleCatalogExtension =
            project.extensions.create("moduleDependencies")
    }
}
