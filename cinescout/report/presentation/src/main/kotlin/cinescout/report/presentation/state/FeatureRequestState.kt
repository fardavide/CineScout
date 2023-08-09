package cinescout.report.presentation.state

import arrow.optics.optics
import cinescout.report.domain.model.ReportLinks
import cinescout.report.presentation.model.TextFieldState
import cinescout.utils.compose.Effect

@optics data class FeatureRequestState(
    val alternativeSolutions: TextFieldState,
    val description: TextFieldState,
    val title: TextFieldState,
    val submitModal: Effect<ReportLinks>
) {
    
    val hasError = listOf(
        alternativeSolutions,
        description,
        title
    ).any { it.error.isSome() }

    companion object {

        val Empty = FeatureRequestState(
            alternativeSolutions = TextFieldState.Empty,
            description = TextFieldState.Empty,
            title = TextFieldState.Empty,
            submitModal = Effect.empty()
        )
    }
}
