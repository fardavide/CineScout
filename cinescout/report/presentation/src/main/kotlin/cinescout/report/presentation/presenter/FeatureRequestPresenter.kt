package cinescout.report.presentation.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import arrow.core.none
import arrow.core.some
import arrow.optics.copy
import cinescout.report.domain.model.FeatureRequestForm
import cinescout.report.domain.usecase.BuildReportLinks
import cinescout.report.presentation.action.FeatureRequestAction
import cinescout.report.presentation.model.FeatureRequestField
import cinescout.report.presentation.model.error
import cinescout.report.presentation.model.text
import cinescout.report.presentation.state.FeatureRequestState
import cinescout.report.presentation.state.alternativeSolutions
import cinescout.report.presentation.state.description
import cinescout.report.presentation.state.title
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.utils.compose.Effect
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class FeatureRequestPresenter(
    private val buildReportLinks: BuildReportLinks
) {

    @Composable
    fun models(actions: Flow<FeatureRequestAction>): FeatureRequestState {
        var state by remember { mutableStateOf(FeatureRequestState.Empty) }

        LaunchedEffect(Unit) {
            actions.collect { action ->
                val newState = when (action) {
                    is FeatureRequestAction.FocusChanged -> cleanError(state, action.field)
                    is FeatureRequestAction.Submit -> {
                        val validatedState = validateAll(
                            state = state,
                            alternativeSolutions = action.alternativeSolutions,
                            description = action.description,
                            title = action.title
                        )
                        when {
                            validatedState.hasError.not() -> validatedState.copy(
                                submitModal = Effect.of(buildReportLinks(validatedState.toForm()))
                            )
                            else -> validatedState
                        }
                    }
                    is FeatureRequestAction.ValidateField -> validate(state, action.field, action.text)
                }
                state = newState
            }
        }

        return state
    }

    private fun cleanError(state: FeatureRequestState, focusField: FeatureRequestField): FeatureRequestState =
        state.copy {
            when (focusField) {
                FeatureRequestField.AlternativeSolutions -> FeatureRequestState.alternativeSolutions.error set none()
                FeatureRequestField.Description -> FeatureRequestState.description.error set none()
                FeatureRequestField.Title -> FeatureRequestState.title.error set none()
            }
        }

    private fun validate(
        state: FeatureRequestState,
        field: FeatureRequestField,
        text: String
    ): FeatureRequestState = state.copy {
        when (field) {
            FeatureRequestField.AlternativeSolutions -> {
                FeatureRequestState.alternativeSolutions.text set text
                FeatureRequestState.alternativeSolutions.error set when {
                    text.isBlank() -> TextRes(string.report_error_empty_alternative_solutions).some()
                    else -> none()
                }
            }
            FeatureRequestField.Description -> {
                FeatureRequestState.description.text set text
                FeatureRequestState.description.error set when {
                    text.isBlank() -> TextRes(string.report_error_empty_description).some()
                    else -> none()
                }
            }
            FeatureRequestField.Title -> {
                FeatureRequestState.title.text set text
                FeatureRequestState.title.error set when {
                    text.isBlank() -> TextRes(string.report_error_empty_title).some()
                    else -> none()
                }
            }
        }
    }

    private fun validateAll(
        state: FeatureRequestState,
        alternativeSolutions: String,
        description: String,
        title: String
    ): FeatureRequestState = state.copy {
        FeatureRequestState.alternativeSolutions.text set alternativeSolutions
        FeatureRequestState.alternativeSolutions.error set when {
            alternativeSolutions.isBlank() -> TextRes(string.report_error_empty_alternative_solutions).some()
            else -> none()
        }
        FeatureRequestState.description.text set description
        FeatureRequestState.description.error set when {
            description.isBlank() -> TextRes(string.report_error_empty_description).some()
            else -> none()
        }
        FeatureRequestState.title.text set title
        FeatureRequestState.title.error set when {
            title.isBlank() -> TextRes(string.report_error_empty_title).some()
            else -> none()
        }
    }

    private fun FeatureRequestState.toForm() = FeatureRequestForm(
        alternativeSolutions = alternativeSolutions.text,
        description = description.text,
        title = title.text
    )
}
