package cinescout.plugins.android

import cinescout.plugins.common.AndroidDefaults
import cinescout.plugins.common.CinescoutAndroidExtension
import cinescout.plugins.common.JvmDefaults
import cinescout.plugins.common.configureAndroidExtension
import cinescout.plugins.util.apply
import cinescout.plugins.util.configure
import cinescout.plugins.util.withType
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Applies a shared configuration to Android targets (apps, libraries, etc...).
 */
class AndroidPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.pluginManager.apply<KotlinAndroidPluginWrapper>()

        target.tasks.withType<KotlinCompile> { task ->
            task.compilerOptions.allWarningsAsErrors.set(JvmDefaults.WARNINGS_AS_ERRORS)
            // Can't use JVM toolchains yet on Android.
            task.kotlinOptions {
                jvmTarget = JvmDefaults.JAVA_VERSION.toString()
                freeCompilerArgs = freeCompilerArgs + AndroidDefaults.FreeCompilerArgs
            }
        }
        configureAndroidKspSources(target)

        target.extensions.configure(::configureAndroidExtension)

        AndroidOptInsExtension.setup(target)
        CinescoutAndroidExtension.setup(target)
    }
}
