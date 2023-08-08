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
import cinescout.report.domain.usecase.BuildGitHubBugReportLink
import cinescout.report.presentation.action.ReportBugAction
import cinescout.report.presentation.model.ReportBugField
import cinescout.report.presentation.model.error
import cinescout.report.presentation.model.text
import cinescout.report.presentation.state.ReportBugState
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
internal class ReportBugPresenter(
    private val buildGitHubBugReportLink: BuildGitHubBugReportLink
) {

    @Composable
    fun models(actions: Flow<ReportBugAction>): ReportBugState {
        var state by remember { mutableStateOf(ReportBugState.Empty) }

        LaunchedEffect(Unit) {
            actions.collect { action ->
                val newState = when (action) {
                    is ReportBugAction.FocusChanged -> cleanError(state, action.field)
                    is ReportBugAction.Submit -> {
                        val validatedState = validateAll(
                            state = state,
                            description = action.description,
                            expectedBehavior = action.expectedBehavior,
                            steps = action.steps,
                            title = action.title
                        )
                        when {
                            validatedState.hasError.not() -> validatedState.copy(
                                openUrl = Effect.of(buildGitHubBugReportLink(validatedState.toForm()))
                            )
                            else -> validatedState
                        }
                    }
                    is ReportBugAction.ValidateField -> validate(state, action.field, action.text)
                }
                state = newState
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
            ReportBugField.Description -> {
                ReportBugState.description.text set text
                ReportBugState.description.error set when {
                    text.isBlank() -> TextRes(string.report_error_empty_description).some()
                    else -> none()
                }
            }
            ReportBugField.ExpectedBehavior -> {
                ReportBugState.expectedBehavior.text set text
                ReportBugState.expectedBehavior.error set when {
                    text.isBlank() -> TextRes(string.report_error_empty_expected_behavior).some()
                    else -> none()
                }
            }
            ReportBugField.Steps -> {
                ReportBugState.steps.text set text
                ReportBugState.steps.error set when {
                    text.isBlank() -> TextRes(string.report_error_empty_steps).some()
                    else -> none()
                }
            }
            ReportBugField.Title -> {
                ReportBugState.title.text set text
                ReportBugState.title.error set when {
                    text.isBlank() -> TextRes(string.report_error_empty_title).some()
                    else -> none()
                }
            }
        }
    }

    private fun validateAll(
        state: ReportBugState,
        description: String,
        expectedBehavior: String,
        steps: String,
        title: String
    ): ReportBugState = state.copy {
        ReportBugState.description.text set description
        ReportBugState.description.error set when {
            description.isBlank() -> TextRes(string.report_error_empty_description).some()
            else -> none()
        }
        ReportBugState.expectedBehavior.text set expectedBehavior
        ReportBugState.expectedBehavior.error set when {
            expectedBehavior.isBlank() -> TextRes(string.report_error_empty_expected_behavior).some()
            else -> none()
        }
        ReportBugState.steps.text set steps
        ReportBugState.steps.error set when {
            steps.isBlank() -> TextRes(string.report_error_empty_steps).some()
            else -> none()
        }
        ReportBugState.title.text set title
        ReportBugState.title.error set when {
            title.isBlank() -> TextRes(string.report_error_empty_title).some()
            else -> none()
        }
    }

    private fun ReportBugState.toForm() = BugReportForm(
        description = description.text,
        expectedBehavior = expectedBehavior.text,
        steps = steps.text,
        title = title.text
    )
}
