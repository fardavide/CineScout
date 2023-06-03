package cinescout.plugins.common

object AndroidDefaults {

    const val ExperimentalFoundationApi = "androidx.compose.foundation.ExperimentalFoundationApi"
    const val ExperimentalLayoutApi = "androidx.compose.foundation.layout.ExperimentalLayoutApi"
    const val ExperimentalPermissionsApi = "com.google.accompanist.permissions.ExperimentalPermissionsApi"
    const val ExperimentalSnapperApi = "dev.chrisbanes.snapper.ExperimentalSnapperApi"
    const val ExperimentalTestApi = "androidx.compose.ui.test.ExperimentalTestApi"
    const val ExperimentalWindowSizeClassApi =
        "androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi"

    val FreeCompilerArgs = KotlinDefaults.FreeCompilerArgs
    val TestFreeCompilerArgs = KotlinDefaults.TestFreeCompilerArgs
    val ComposeFreeCompilerArgs = FreeCompilerArgs + listOf(
        "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
    )

    const val APPLICATION_ID = "studio.forface.cinescout2"
    const val BUILD_TOOLS = "33.0.2"
    const val COMPILE_SDK = 33
    const val MIN_SDK = 26
    const val TARGET_SDK = 33
}
