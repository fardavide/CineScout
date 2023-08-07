package cinescout.report.presentation.state

import arrow.optics.optics
import cinescout.report.presentation.model.TextFieldState

@optics data class ReportBugState(
    val description: TextFieldState,
    val expectedBehavior: TextFieldState,
    val isSubmitEnabled: Boolean,
    val steps: TextFieldState,
    val title: TextFieldState
) {

    companion object {

        val Empty = ReportBugState(
            description = TextFieldState.Empty,
            expectedBehavior = TextFieldState.Empty,
            isSubmitEnabled = false,
            steps = TextFieldState.Empty,
            title = TextFieldState.Empty
        )
    }
}
