package cinescout.plugins.jvm

import cinescout.plugins.common.JvmDefaults
import cinescout.plugins.common.KotlinDefaults
import cinescout.plugins.util.apply
import cinescout.plugins.util.configure
import cinescout.plugins.util.sourceSets
import cinescout.plugins.util.withType
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("unused")
internal class JvmPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.pluginManager.apply<KotlinPluginWrapper>()

        target.tasks.withType<KotlinCompile> { task ->
            task.compilerOptions.allWarningsAsErrors.set(JvmDefaults.WARNINGS_AS_ERRORS)
            task.kotlinOptions {
                freeCompilerArgs = freeCompilerArgs + KotlinDefaults.FreeCompilerArgs
            }
        }

        target.extensions.configure<KotlinTopLevelExtension> { ext ->
            ext.jvmToolchain(JvmDefaults.JAVA_VERSION)
        }

        target.sourceSets.configureEach { sourceSet ->
            sourceSet.allSource.srcDirs(
                "build/generated/ksp/${sourceSet.name}/kotlin"
            )
        }
    }
}
