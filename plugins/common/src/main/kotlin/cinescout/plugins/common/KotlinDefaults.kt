package cinescout.plugins.common

object KotlinDefaults {

    const val ContextReceiversCompilerArg = "-Xcontext-receivers"
    const val DelicateCoroutinesApi = "kotlinx.coroutines.DelicateCoroutinesApi"
    const val ExperimentalCoroutinesApi = "kotlinx.coroutines.ExperimentalCoroutinesApi"
    const val ExperimentalKermitApi = "co.touchlab.kermit.ExperimentalKermitApi"
    const val ExperimentalPagingApi = "androidx.paging.ExperimentalPagingApi"
    const val ExperimentalStdlibApi = "kotlin.ExperimentalStdlibApi"
    const val FlowPreview = "kotlinx.coroutines.FlowPreview"

    val DefaultOptIns = listOf(
        "kotlin.experimental.ExperimentalTypeInference",
        "kotlin.time.ExperimentalTime"
    )
    val TestOptIns = listOf(
        "cinescout.CineScoutTestApi"
    )

    val FreeCompilerArgs = DefaultOptIns
        .map { annotationName -> "-opt-in=$annotationName" } +
        ContextReceiversCompilerArg

    val TestFreeCompilerArgs = TestOptIns
        .map { annotationName -> "-opt-in=$annotationName" }
}
