package cinescout.plugins.common

object KotlinDefaults {

    val FreeCompilerArgs = listOf(
        "-opt-in=kotlin.time.ExperimentalTime",
        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        "-opt-in=kotlinx.coroutines.FlowPreview",
        "-Xcontext-receivers"
    )
}
