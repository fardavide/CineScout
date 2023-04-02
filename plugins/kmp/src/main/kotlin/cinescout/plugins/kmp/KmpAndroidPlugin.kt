package cinescout.plugins.kmp

import cinescout.plugins.common.CinescoutAndroidExtension
import cinescout.plugins.common.JvmDefaults
import cinescout.plugins.common.configureAndroidExtension
import cinescout.plugins.common.configureAndroidKspSources
import cinescout.plugins.util.apply
import cinescout.plugins.util.configure
import cinescout.plugins.util.withType
import com.google.devtools.ksp.gradle.KspTaskJvm
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTargetPreset
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

/**
 * Applies a shared Kotlin multi-platform configuration to the given project.
 * This plugin supports the following compilation targets:
 * - JVM
 */
@Suppress("unused")
internal class KmpAndroidPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target.pluginManager) {
            apply<KmpPlugin>()
            apply("com.android.library")
        }

        target.extensions.configure<KotlinMultiplatformExtension> { ext ->
            ext.targetFromPreset(AndroidTargetPreset(target), ::configureAndroidTarget)
        }

        target.extensions.configure(::configureAndroidExtension)
        configureAndroidKspSources(target)
        CinescoutAndroidExtension.setup(target)

        // TODO workaround for https://issuetracker.google.com/issues/269089135
        target.tasks.withType<KspTaskJvm> { task ->
            task.mustRunAfter(target.tasks.named("compileDebugKotlinAndroid"))
            task.mustRunAfter(target.tasks.named("compileReleaseKotlinAndroid"))
        }
    }

    private fun configureAndroidTarget(target: KotlinAndroidTarget) {
        target.compilations.all { compilation ->
            compilation.kotlinOptions {
                jvmTarget = JvmDefaults.JAVA_VERSION.toString()
            }
            compilation.compilerOptions.configure {
                allWarningsAsErrors.set(JvmDefaults.WARNINGS_AS_ERRORS)
            }
        }
    }

    private fun configureJvmTarget(target: KotlinJvmTarget) {
        target.compilations.all { compilation ->
            compilation.compilerOptions.configure {
                allWarningsAsErrors.set(JvmDefaults.WARNINGS_AS_ERRORS)
                jvmTarget.set(JvmTarget.fromTarget(JvmDefaults.JAVA_VERSION.toString()))
            }
        }
    }
}

private class AndroidTargetPreset(project: Project) : KotlinAndroidTargetPreset(project)
