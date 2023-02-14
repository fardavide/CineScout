package cinescout.plugins.modulecatalog

import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
internal class ModuleCatalogPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        ModuleCatalogExtension.setup(target)
    }
}
