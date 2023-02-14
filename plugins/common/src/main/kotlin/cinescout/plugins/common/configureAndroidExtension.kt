package cinescout.plugins.common

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion

@Suppress("UnstableApiUsage")
fun configureAndroidExtension(ext: CommonExtension<*, *, *, *>) {
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
