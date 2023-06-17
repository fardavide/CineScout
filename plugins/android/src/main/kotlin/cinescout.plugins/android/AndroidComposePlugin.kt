package cinescout.plugins.android

import cinescout.plugins.common.AndroidDefaults
import cinescout.plugins.util.configure
import cinescout.plugins.util.libsCatalog
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
        target.pluginManager.apply("app.cash.molecule")
        target.tasks.withType<KotlinCompile> { task ->
            task.kotlinOptions {
                freeCompilerArgs += AndroidDefaults.ComposeFreeCompilerArgs +
                    // TODO: Remove this once we upgrade Molecule to support Kotlin 1.8.22
                    "-P" +
                    "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=1.8.22"
            }
        }
    }

    @Suppress("UnstableApiUsage")
    private fun configureComposeOptions(libsCatalog: VersionCatalog, ext: TestedExtension) {
        ext.buildFeatures.compose = true
        val composeCompilerVersion = libsCatalog.findVersion("composeCompiler").get().toString()
        ext.composeOptions.kotlinCompilerExtensionVersion = composeCompilerVersion
    }
}
