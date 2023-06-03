package cinescout.plugins.kmp

import cinescout.plugins.common.JvmDefaults
import cinescout.plugins.common.KotlinDefaults
import cinescout.plugins.util.apply
import cinescout.plugins.util.configure
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmTargetPreset
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

/**
 * Applies a shared Kotlin multi-platform configuration to the given project.
 * This plugin supports the following compilation targets:
 * - JVM
 */
internal class KmpPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.pluginManager.apply<KotlinMultiplatformPluginWrapper>()

        target.extensions.configure<KotlinMultiplatformExtension> { ext ->
            ext.apply {
                jvmToolchain(JvmDefaults.JAVA_VERSION)
                targetFromPreset(KotlinJvmTargetPreset(target), ::configureJvmTarget)
                sourceSets.all { sourceSet ->
                    for (annotationName in KotlinDefaults.DefaultOptIns) {
                        sourceSet.languageSettings.optIn(annotationName)
                    }
                    if ("Test" in sourceSet.name) {
                        for (annotationName in KotlinDefaults.TestOptIns) {
                            sourceSet.languageSettings.optIn(annotationName)
                        }
                    }
                }
            }
        }
        KmpOptInsExtension.setup(target)
    }

    private fun configureJvmTarget(target: KotlinJvmTarget) {
        target.compilations.all { compilation ->
            compilation.compilerOptions.configure {
                allWarningsAsErrors.set(JvmDefaults.WARNINGS_AS_ERRORS)
            }
            compilation.kotlinOptions {
                freeCompilerArgs = freeCompilerArgs + KotlinDefaults.ContextReceiversCompilerArg
            }
        }
    }
}
