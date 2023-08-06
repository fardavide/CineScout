package cinescout.plugins.common

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

fun configureAndroidExtension(target: Project, ext: CommonExtension<*, *, *, *, *>) {
    if (ext !is ApplicationExtension) {
        val namespace = target.path.removePrefix(":").replace(":", ".")
        ext.namespace = namespace
    }
    ext.buildToolsVersion = AndroidDefaults.BUILD_TOOLS
    ext.compileSdk = AndroidDefaults.COMPILE_SDK
    ext.defaultConfig.minSdk = AndroidDefaults.MIN_SDK

    val javaVersion = JavaVersion.toVersion(JvmDefaults.JAVA_VERSION)
    ext.compileOptions.apply {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    ext.defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    ext.lint {
        checkAllWarnings = true
        warningsAsErrors = true
        disable += listOf(
            "DuplicateStrings",
            "InvalidPackage",
            "MissingPermission",
            "SyntheticAccessor"
        )
    }

    ext.packaging.resources.excludes += listOf(
        "META-INF/LICENSE-notice.md",
        "META-INF/LICENSE.md",
        "META-INF/licenses/ASM",
        "META-INF/{AL2.0,LGPL2.1}",
        "win32-x86-64/attach_hotspot_windows.dll",
        "win32-x86/attach_hotspot_windows.dll"
    )
}
