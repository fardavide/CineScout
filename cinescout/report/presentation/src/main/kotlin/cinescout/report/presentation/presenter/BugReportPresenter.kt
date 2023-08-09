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
import cinescout.report.domain.model.BugReportForm
import cinescout.report.domain.usecase.BuildReportLinks
import cinescout.report.presentation.action.BugReportAction
import cinescout.report.presentation.model.BugReportField
import cinescout.report.presentation.model.error
import cinescout.report.presentation.model.text
import cinescout.report.presentation.state.BugReportState
import cinescout.report.presentation.state.description
import cinescout.report.presentation.state.expectedBehavior
import cinescout.report.presentation.state.steps
import cinescout.report.presentation.state.title
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.utils.compose.Effect
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class BugReportPresenter(
    private val buildReportLinks: BuildReportLinks
) {

    @Composable
    fun models(actions: Flow<BugReportAction>): BugReportState {
        var state by remember { mutableStateOf(BugReportState.Empty) }

        LaunchedEffect(Unit) {
            actions.collect { action ->
                val newState = when (action) {
                    is BugReportAction.FocusChanged -> cleanError(state, action.field)
                    is BugReportAction.Submit -> {
                        val validatedState = validateAll(
                            state = state,
                            description = action.description,
                            expectedBehavior = action.expectedBehavior,
                            steps = action.steps,
                            title = action.title
                        )
                        when {
                            validatedState.hasError.not() -> validatedState.copy(
                                submitModal = Effect.of(buildReportLinks(validatedState.toForm()))
                            )
                            else -> validatedState
                        }
                    }
                    is BugReportAction.ValidateField -> validate(state, action.field, action.text)
                }
                state = newState
            }
        }

        return state
    }

    private fun cleanError(state: BugReportState, focusField: BugReportField): BugReportState = state.copy {
        when (focusField) {
            BugReportField.Description -> BugReportState.description.error set none()
            BugReportField.ExpectedBehavior -> BugReportState.expectedBehavior.error set none()
            BugReportField.Steps -> BugReportState.steps.error set none()
            BugReportField.Title -> BugReportState.title.error set none()
        }
    }

    private fun validate(
        state: BugReportState,
        field: BugReportField,
        text: String
    ): BugReportState = state.copy {
        when (field) {
            BugReportField.Description -> {
                BugReportState.description.text set text
                BugReportState.description.error set when {
                    text.isBlank() -> TextRes(string.report_error_empty_description).some()
                    else -> none()
                }
            }
            BugReportField.ExpectedBehavior -> {
                BugReportState.expectedBehavior.text set text
                BugReportState.expectedBehavior.error set when {
                    text.isBlank() -> TextRes(string.report_error_empty_expected_behavior).some()
                    else -> none()
                }
            }
            BugReportField.Steps -> {
                BugReportState.steps.text set text
                BugReportState.steps.error set when {
                    text.isBlank() -> TextRes(string.report_error_empty_steps).some()
                    else -> none()
                }
            }
            BugReportField.Title -> {
                BugReportState.title.text set text
                BugReportState.title.error set when {
                    text.isBlank() -> TextRes(string.report_error_empty_title).some()
                    else -> none()
                }
            }
        }
    }

    private fun validateAll(
        state: BugReportState,
        description: String,
        expectedBehavior: String,
        steps: String,
        title: String
    ): BugReportState = state.copy {
        BugReportState.description.text set description
        BugReportState.description.error set when {
            description.isBlank() -> TextRes(string.report_error_empty_description).some()
            else -> none()
        }
        BugReportState.expectedBehavior.text set expectedBehavior
        BugReportState.expectedBehavior.error set when {
            expectedBehavior.isBlank() -> TextRes(string.report_error_empty_expected_behavior).some()
            else -> none()
        }
        BugReportState.steps.text set steps
        BugReportState.steps.error set when {
            steps.isBlank() -> TextRes(string.report_error_empty_steps).some()
            else -> none()
        }
        BugReportState.title.text set title
        BugReportState.title.error set when {
            title.isBlank() -> TextRes(string.report_error_empty_title).some()
            else -> none()
        }
    }

    private fun BugReportState.toForm() = BugReportForm(
        description = description.text,
        expectedBehavior = expectedBehavior.text,
        steps = steps.text,
        title = title.text
    )
}
