import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import javax.inject.Inject

open class CineScoutModulesCatalogExtension @Inject constructor(
    private val project: Project
) : ModulesCatalog(project) {

    operator fun Any.invoke() {
        with(module) {
            project.getMultiplatformExtension()?.sourceSets?.named(sourceSetName) {
                dependencies {
                    implementation(project.rootProject.project(normalizedPath))
                }
            }
        }
    }

    companion object {

        fun setup(project: Project): CineScoutModulesCatalogExtension =
            project.extensions.create("moduleDependencies")
    }
}
