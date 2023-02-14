package cinescout.plugins.android

import cinescout.plugins.common.KotlinDefaults

object AndroidDefaults {

    val FreeCompilerArgs = KotlinDefaults.FreeCompilerArgs
    val ComposeFreeCompilerArgs = FreeCompilerArgs + listOf(
        "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
    )

    const val APPLICATION_ID = "studio.forface.cinescout"
    const val BUILD_TOOLS = "33.0.2"
    const val COMPILE_SDK = 33
    const val MIN_SDK = 26
    const val TARGET_SDK = 33
}
