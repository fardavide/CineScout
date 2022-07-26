import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import java.util.Locale
import javax.inject.Inject

open class CineScoutModulesCatalogExtension @Inject constructor(
    private val project: Project
) : ModulesCatalog(project) {

    operator fun Any.invoke() {
        with(module) {
            if (project.findMultiplatformExtension() != null) {
                addMultiplatformDependency(sourceSetName)
            } else {
                addJvmDependency(sourceSetName)
            }
        }
    }

    private fun Module.addMultiplatformDependency(sourceSetName: String) {
        if (sourceSetName == "androidMain") {
            project.dependencies.add("implementation", project.rootProject.project(normalizedPath))
        } else {
            project.getMultiplatformExtension().sourceSets.named(sourceSetName) {
                dependencies {
                    implementation(project.rootProject.project(normalizedPath))
                }
            }
        }
    }

    private fun Module.addJvmDependency(sourceSetName: String) {
        val configurationName =
            if (sourceSetName == CommonMainSourceSetName) {
                "implementation"
            } else {
                sourceSetName.substringAfter("common").decapitalize(Locale.ROOT)
            }
        project.dependencies {
            add(configurationName, project.rootProject.project(normalizedPath))
        }
    }

    companion object {

        fun setup(project: Project): CineScoutModulesCatalogExtension =
            project.extensions.create("moduleDependencies")
    }
}
