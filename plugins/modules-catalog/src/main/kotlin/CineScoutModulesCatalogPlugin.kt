import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class CineScoutModulesCatalogPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.subprojects.forEach(CineScoutModulesCatalogExtension::setup)
    }
}
