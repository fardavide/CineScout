package cinescout.plugins.common

object KotlinDefaults {

    const val ContextReceiversCompilerArg = "-Xcontext-receivers"
    const val ExperimentalCoroutinesApi = "kotlinx.coroutines.ExperimentalCoroutinesApi"

    const val FlowPreview = "kotlinx.coroutines.FlowPreview"

    val OptIns = listOf(
        "kotlin.experimental.ExperimentalTypeInference",
        "kotlin.time.ExperimentalTime"
    )

    val FreeCompilerArgs = OptIns.map { annotationName -> "-opt-in=$annotationName" } + ContextReceiversCompilerArg
}
