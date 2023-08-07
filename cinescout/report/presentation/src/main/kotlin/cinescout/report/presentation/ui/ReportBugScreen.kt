package cinescout.report.presentation.ui

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import arrow.core.getOrElse
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.ui.BackBottomBar
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.report.presentation.action.ReportBugAction
import cinescout.report.presentation.model.ReportBugField
import cinescout.report.presentation.model.TextFieldState
import cinescout.report.presentation.state.ReportBugState
import cinescout.report.presentation.viewmodel.ReportBugViewModel
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.resources.string
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReportBugScreen(back: () -> Unit, modifier: Modifier = Modifier) {
    val viewModel: ReportBugViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateLifecycleAware()
    ReportBugScreen(
        state = state,
        actions = ReportBugScreen.Actions(
            back = back,
            onFieldFocused = { viewModel.submit(ReportBugAction.FocusChanged(it)) },
            validateField = { field, text -> viewModel.submit(ReportBugAction.ValidateField(field, text)) }
        ),
        modifier = modifier
    )
}

@Composable
private fun ReportBugScreen(
    state: ReportBugState,
    actions: ReportBugScreen.Actions,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.testTag(TestTag.ReportBug),
        topBar = { TopBar() },
        bottomBar = { BackBottomBar(back = actions.back) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = Dimens.Margin.medium, vertical = Dimens.Margin.small),
            verticalArrangement = Arrangement.spacedBy(Dimens.Margin.medium)
        ) {
            TextField(
                state = state.title,
                label = TextRes(string.report_title),
                onFocused = { actions.onFieldFocused(ReportBugField.Title) },
                validate = { actions.validateField(ReportBugField.Title, it) }
            )
            TextField(
                state = state.description,
                label = TextRes(string.report_description),
                minLines = 3,
                onFocused = { actions.onFieldFocused(ReportBugField.Description) },
                validate = { actions.validateField(ReportBugField.Description, it) }
            )
            TextField(
                state = state.steps,
                label = TextRes(string.report_steps),
                minLines = 2,
                onFocused = { actions.onFieldFocused(ReportBugField.Steps) },
                validate = { actions.validateField(ReportBugField.Steps, it) }
            )
            TextField(
                state = state.expectedBehavior,
                label = TextRes(string.report_expected_behavior),
                minLines = 2,
                onFocused = { actions.onFieldFocused(ReportBugField.ExpectedBehavior) },
                validate = { actions.validateField(ReportBugField.ExpectedBehavior, it) }
            )
        }
    }
}

@Composable
private fun TopBar() {
    CenterAlignedTopAppBar(title = { Text(text = stringResource(id = string.report_report_bug)) })
}

@Composable
private fun TextField(
    state: TextFieldState,
    label: TextRes,
    onFocused: () -> Unit,
    minLines: Int = 1,
    validate: (text: String) -> Unit
) {
    val text = remember(state.text) { state.text }
    var value by remember(text) { mutableStateOf(TextFieldValue(text = text, selection = TextRange(text.length))) }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusable()
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    onFocused()
                } else {
                    validate(value.text)
                }
            },
        value = value,
        onValueChange = { value = it },
        minLines = minLines,
        isError = state.error.isSome(),
        label = {
            val textRes = state.error.getOrElse { label }
            Text(text = string(textRes))
        }
    )
}

object ReportBugScreen {

    data class Actions(
        val back: () -> Unit,
        val onFieldFocused: (field: ReportBugField) -> Unit,
        val validateField: (field: ReportBugField, text: String) -> Unit
    ) {

        companion object {

            val Empty = Actions(
                back = {},
                onFieldFocused = {},
                validateField = { _, _ -> }
            )
        }
    }
}

@Preview
@Composable
private fun ReportBugScreenPreview() {
    CineScoutTheme {
        ReportBugScreen(
            state = ReportBugState.Empty,
            actions = ReportBugScreen.Actions.Empty
        )
    }
}
