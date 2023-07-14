package cinescout.plugins.modulecatalog

import cinescout.plugins.util.create
import org.gradle.api.Project
import javax.inject.Inject

@Deprecated("Use regular Gradle dependency management with Typesafe Project Accessors instead")
open class ModuleCatalogExtension @Inject constructor(private val project: Project) {

    @Deprecated("Use regular Gradle dependency management instead with Typesafe Project Accessors")
    fun api(module: String) {
        val configurations = project.configurations
        val configurationName = when {
            "commonMainApi" in configurations.names -> "commonMainApi"
            "api" in configurations.names -> "api"
            else -> error("No configuration found for module $module")
        }
        project.dependencies.add(configurationName, project.project(":cinescout:$module"))
    }

    @Deprecated("Use regular Gradle dependency management instead with Typesafe Project Accessors")
    fun implementation(module: String) {
        val configurations = project.configurations
        val configurationName = when {
            "commonMainImplementation" in configurations.names -> "commonMainImplementation"
            "implementation" in configurations.names -> "implementation"
            else -> error("No configuration found for module $module")
        }
        project.dependencies.add(configurationName, project.project(":cinescout:$module"))
    }

    @Deprecated("Use regular Gradle dependency management instead with Typesafe Project Accessors")
    fun testImplementation(module: String) {
        val configurations = project.configurations
        val configurationName = when {
            "commonTestImplementation" in configurations.names -> "commonTestImplementation"
            "testImplementation" in configurations.names -> "testImplementation"
            else -> error("No configuration found for module $module")
        }
        project.dependencies.add(configurationName, project.project(":cinescout:$module"))
    }

    @Deprecated("Use regular Gradle dependency management instead with Typesafe Project Accessors")
    fun androidTestImplementation(module: String) {
        project.dependencies.add("androidTestImplementation", project.project(":cinescout:$module"))
    }

    companion object {

        fun setup(project: Project): ModuleCatalogExtension = project.extensions.create("moduleDependencies")
    }
}
