package cinescout.plugins.android

import cinescout.plugins.common.AndroidDefaults
import cinescout.plugins.util.composeCompilerVersion
import cinescout.plugins.util.configure
import cinescout.plugins.util.libsCatalog
import cinescout.plugins.util.slackComposeLintLibrary
import cinescout.plugins.util.withType
import com.android.build.gradle.TestedExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal class AndroidComposePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val libsCatalog = target.libsCatalog
        target.extensions.configure<TestedExtension> { ext -> configureComposeOptions(libsCatalog, ext) }
        target.dependencies.add("lintChecks", libsCatalog.slackComposeLintLibrary)
        target.pluginManager.apply("app.cash.molecule")
        target.tasks.withType<KotlinCompile> { task ->
            task.kotlinOptions {
                freeCompilerArgs += AndroidDefaults.ComposeFreeCompilerArgs
            }
        }
    }

    @Suppress("UnstableApiUsage")
    private fun configureComposeOptions(libsCatalog: VersionCatalog, ext: TestedExtension) {
        ext.buildFeatures.compose = true
        ext.composeOptions.kotlinCompilerExtensionVersion = libsCatalog.composeCompilerVersion
    }
}
