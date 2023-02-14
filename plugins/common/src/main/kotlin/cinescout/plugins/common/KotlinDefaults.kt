package cinescout.plugins.common

object KotlinDefaults {

    val OptIns = listOf(
        "kotlin.experimental.ExperimentalTypeInference",
        "kotlin.time.ExperimentalTime",
        "kotlinx.coroutines.ExperimentalCoroutinesApi",
        "kotlinx.coroutines.FlowPreview"
    )

    val FreeCompilerArgs = OptIns.map { annotationName -> "-opt-in=$annotationName" } + "-Xcontext-receivers"
}
