package cinescout.report.presentation.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import arrow.core.getOrElse
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.ui.BackBottomBar
import cinescout.design.util.NoContentDescription
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.report.presentation.action.ReportBugAction
import cinescout.report.presentation.model.ReportBugField
import cinescout.report.presentation.model.TextFieldState
import cinescout.report.presentation.state.ReportBugState
import cinescout.report.presentation.viewmodel.ReportBugViewModel
import cinescout.resources.R.drawable
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.resources.string
import cinescout.utils.compose.Consume
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReportBugScreen(back: () -> Unit, modifier: Modifier = Modifier) {
    val viewModel: ReportBugViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateLifecycleAware()
    val context = LocalContext.current
    Consume(state.openUrl) { url ->
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
    ReportBugScreen(
        state = state,
        actions = ReportBugScreen.Actions(
            back = back,
            onFieldFocused = { viewModel.submit(ReportBugAction.FocusChanged(it)) },
            submit = { description, expectedBehavior, steps, title ->
                viewModel.submit(
                    ReportBugAction.Submit(
                        description,
                        expectedBehavior,
                        steps,
                        title
                    )
                )
            },
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
    var title by remember(state.title) { mutableStateOf(state.title) }
    var description by remember(state.description) { mutableStateOf(state.description) }
    var steps by remember(state.steps) { mutableStateOf(state.steps) }
    var expectedBehavior by remember(state.expectedBehavior) { mutableStateOf(state.expectedBehavior) }
    Scaffold(
        modifier = modifier
            .testTag(TestTag.ReportBug)
            .imePadding(),
        topBar = { TopBar() },
        bottomBar = { BackBottomBar(back = actions.back) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(stringResource(id = string.report_send)) },
                icon = {
                    Icon(
                        painter = painterResource(id = drawable.ic_send),
                        contentDescription = NoContentDescription
                    )
                },
                onClick = {
                    actions.submit(description.text, expectedBehavior.text, steps.text, title.text)
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = Dimens.Margin.medium)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Dimens.Margin.medium)
        ) {
            Spacer(modifier = Modifier.padding(top = Dimens.Margin.small))
            TextField(
                state = title,
                onStateChange = { title = it },
                label = TextRes(string.report_title),
                onFocused = { actions.onFieldFocused(ReportBugField.Title) },
                validate = { actions.validateField(ReportBugField.Title, title.text) }
            )
            TextField(
                state = description,
                onStateChange = { description = it },
                label = TextRes(string.report_description),
                minLines = 3,
                onFocused = { actions.onFieldFocused(ReportBugField.Description) },
                validate = { actions.validateField(ReportBugField.Description, description.text) }
            )
            TextField(
                state = steps,
                onStateChange = { steps = it },
                label = TextRes(string.report_steps),
                minLines = 2,
                onFocused = { actions.onFieldFocused(ReportBugField.Steps) },
                validate = { actions.validateField(ReportBugField.Steps, steps.text) }
            )
            TextField(
                state = expectedBehavior,
                onStateChange = { expectedBehavior = it },
                label = TextRes(string.report_expected_behavior),
                minLines = 2,
                onFocused = { actions.onFieldFocused(ReportBugField.ExpectedBehavior) },
                validate = { actions.validateField(ReportBugField.ExpectedBehavior, expectedBehavior.text) }
            )
            Spacer(modifier = Modifier.padding(top = Dimens.Margin.xxxLarge))
        }
    }
}

@Composable
private fun TopBar() {
    CenterAlignedTopAppBar(title = { Text(text = stringResource(id = string.report_report_bug)) })
}

@Composable
@Suppress("UseComposableActions")
private fun TextField(
    state: TextFieldState,
    onStateChange: (TextFieldState) -> Unit,
    label: TextRes,
    onFocused: () -> Unit,
    minLines: Int = 1,
    validate: () -> Unit
) {
    val text = remember(state.text) { state.text }
    var value by remember(text) { mutableStateOf(TextFieldValue(text = text, selection = TextRange(text.length))) }
    var hadFocus by remember { mutableStateOf(false) }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusable()
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    hadFocus = true
                    onFocused()
                } else {
                    if (hadFocus) validate()
                }
            },
        value = value,
        onValueChange = {
            value = it
            onStateChange(state.copy(text = it.text))
        },
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
        val submit: (description: String, expectedBehavior: String, steps: String, title: String) -> Unit,
        val validateField: (field: ReportBugField, text: String) -> Unit
    ) {

        companion object {

            val Empty = Actions(
                back = {},
                onFieldFocused = {},
                submit = { _, _, _, _ -> },
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
