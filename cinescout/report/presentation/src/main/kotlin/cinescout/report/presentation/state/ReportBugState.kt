package cinescout.report.presentation.state

import arrow.optics.optics
import cinescout.report.domain.model.ReportLinks
import cinescout.report.presentation.model.TextFieldState
import cinescout.utils.compose.Effect

@optics data class ReportBugState(
    val description: TextFieldState,
    val expectedBehavior: TextFieldState,
    val steps: TextFieldState,
    val title: TextFieldState,
    val submitModal: Effect<ReportLinks>
) {
    
    val hasError = listOf(
        description,
        expectedBehavior,
        steps,
        title
    ).any { it.error.isSome() }

    companion object {

        val Empty = ReportBugState(
            description = TextFieldState.Empty,
            expectedBehavior = TextFieldState.Empty,
            steps = TextFieldState.Empty,
            title = TextFieldState.Empty,
            submitModal = Effect.empty()
        )
    }
}
