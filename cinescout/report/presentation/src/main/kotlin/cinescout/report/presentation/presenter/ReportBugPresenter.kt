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
import cinescout.report.presentation.action.ReportBugAction
import cinescout.report.presentation.model.ReportBugField
import cinescout.report.presentation.model.error
import cinescout.report.presentation.state.ReportBugState
import cinescout.report.presentation.state.description
import cinescout.report.presentation.state.expectedBehavior
import cinescout.report.presentation.state.steps
import cinescout.report.presentation.state.title
import cinescout.resources.R.string
import cinescout.resources.TextRes
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class ReportBugPresenter {

    @Composable
    fun models(actions: Flow<ReportBugAction>): ReportBugState {
        var state by remember { mutableStateOf(ReportBugState.Empty) }

        LaunchedEffect(Unit) {
            actions.collect { action ->
                when (action) {
                    is ReportBugAction.FocusChanged -> state = cleanError(state, action.field)
                    ReportBugAction.Submit -> TODO()
                    is ReportBugAction.ValidateField -> state = validate(state, action.field, action.text)
                    is ReportBugAction.ValidateAllFields -> state = validateAll(state)
                }
            }
        }

        return state
    }

    private fun cleanError(state: ReportBugState, focusField: ReportBugField): ReportBugState = state.copy {
        when (focusField) {
            ReportBugField.Description -> ReportBugState.description.error set none()
            ReportBugField.ExpectedBehavior -> ReportBugState.expectedBehavior.error set none()
            ReportBugField.Steps -> ReportBugState.steps.error set none()
            ReportBugField.Title -> ReportBugState.title.error set none()
        }
    }

    private fun validate(
        state: ReportBugState,
        field: ReportBugField,
        text: String
    ): ReportBugState = state.copy {
        when (field) {
            ReportBugField.Description -> ReportBugState.description.error set when {
                text.isBlank() -> TextRes(string.report_error_empty_description).some()
                else -> none()
            }
            ReportBugField.ExpectedBehavior -> ReportBugState.expectedBehavior.error set when {
                text.isBlank() -> TextRes(string.report_error_empty_expected_behavior).some()
                else -> none()
            }
            ReportBugField.Steps -> ReportBugState.steps.error set when {
                text.isBlank() -> TextRes(string.report_error_empty_steps).some()
                else -> none()
            }
            ReportBugField.Title -> ReportBugState.title.error set when {
                text.isBlank() -> TextRes(string.report_error_empty_title).some()
                else -> none()
            }
        }
    }

    private fun validateAll(state: ReportBugState): ReportBugState = state.copy {
        ReportBugState.description.error set when {
            state.description.text.isBlank() -> TextRes(string.report_error_empty_description).some()
            else -> none()
        }
        ReportBugState.expectedBehavior.error set when {
            state.expectedBehavior.text.isBlank() -> TextRes(string.report_error_empty_expected_behavior).some()
            else -> none()
        }
        ReportBugState.steps.error set when {
            state.steps.text.isBlank() -> TextRes(string.report_error_empty_steps).some()
            else -> none()
        }
        ReportBugState.title.error set when {
            state.title.text.isBlank() -> TextRes(string.report_error_empty_title).some()
            else -> none()
        }
    }
}
