package cinescout.plugins.android

import cinescout.plugins.common.JvmDefaults
import cinescout.plugins.util.configure
import cinescout.plugins.util.create
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import javax.inject.Inject

open class CinescoutAndroidExtension @Inject constructor(private val project: Project) {

    fun namespace(namespace: String) {
        project.extensions.configure<CommonExtension<*, *, *, *>> { ext ->
            ext.namespace = namespace
        }
    }

    companion object {

        fun setup(project: Project): CinescoutAndroidExtension =
            project.extensions.create("cinescoutAndroid")
    }
}

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
