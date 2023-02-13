package cinescout.plugins.android

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import shuttle.plugins.common.JvmDefaults
import shuttle.plugins.common.KotlinDefaults
import shuttle.plugins.util.apply
import shuttle.plugins.util.configure
import shuttle.plugins.util.withType

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
                freeCompilerArgs = freeCompilerArgs + KotlinDefaults.FreeCompilerArgs
            }
        }

        target.extensions.configure(::configureAndroidExtension)
    }
}
