package cinescout.plugins.android

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
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
internal class AndroidPlugin : Plugin<Project> {
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

    @Suppress("UnstableApiUsage")
    private fun configureAndroidExtension(ext: CommonExtension<*, *, *, *>) {
        ext.buildToolsVersion = AndroidDefaults.BUILD_TOOLS
        ext.compileSdk = AndroidDefaults.COMPILE_SDK
        ext.defaultConfig.minSdk = AndroidDefaults.MIN_SDK

        val javaVersion = JavaVersion.toVersion(JvmDefaults.JAVA_VERSION)
        ext.compileOptions.apply {
            // Can't use JVM toolchains yet on Android.
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
        }

        ext.defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ext.lint.warningsAsErrors = true

        ext.packagingOptions.resources.excludes.addAll(
            listOf(
                "META-INF/{AL2.0,LGPL2.1}",
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md"
            )
        )
    }
}
