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
import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Applies a shared configuration to Android targets (apps, libraries, etc...).
 */
class AndroidPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target.pluginManager) {
            apply<KotlinAndroidPluginWrapper>()
            apply("io.github.flank.gradle.simple-flank")
        }

        target.extensions.configure<KotlinTopLevelExtension> { ext ->
            ext.jvmToolchain(JvmDefaults.JAVA_VERSION)
        }

        target.tasks.withType<KotlinCompile> { task ->
            task.compilerOptions {
                allWarningsAsErrors.set(JvmDefaults.WARNINGS_AS_ERRORS)
                jvmTarget.set(JvmDefaults.Target)
            }
            task.kotlinOptions {
                freeCompilerArgs = freeCompilerArgs + AndroidDefaults.FreeCompilerArgs
            }
        }

        with(target) {
            extensions.configure(::configureAndroidExtension)
            extensions.configure(::configureSimpleFlankExtension)
        }

        AndroidOptInsExtension.setup(target)
        CinescoutAndroidExtension.setup(target)
    }
}
