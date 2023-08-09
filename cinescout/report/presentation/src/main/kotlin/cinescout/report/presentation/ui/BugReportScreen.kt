package cinescout.report.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.ui.BackBottomBar
import cinescout.design.util.NoContentDescription
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.report.domain.model.ReportLinks
import cinescout.report.presentation.action.BugReportAction
import cinescout.report.presentation.model.BugReportField
import cinescout.report.presentation.state.BugReportState
import cinescout.report.presentation.viewmodel.BugReportViewModel
import cinescout.resources.R.drawable
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.utils.compose.Consume
import org.koin.androidx.compose.koinViewModel

@Composable
fun BugReportScreen(back: () -> Unit, modifier: Modifier = Modifier) {
    val viewModel: BugReportViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateLifecycleAware()

    var modalLinks: ReportLinks? by remember { mutableStateOf(null) }
    Consume(state.submitModal) { links ->
        modalLinks = links
    }
    if (modalLinks != null) {
        ReportChooserModal(links = checkNotNull(modalLinks), onDismiss = { modalLinks = null })
    }
    BugReportScreen(
        state = state,
        actions = BugReportScreen.Actions(
            back = back,
            onFieldFocused = { viewModel.submit(BugReportAction.FocusChanged(it)) },
            submit = { description, expectedBehavior, steps, title ->
                viewModel.submit(
                    BugReportAction.Submit(
                        description = description,
                        expectedBehavior = expectedBehavior,
                        steps = steps,
                        title = title
                    )
                )
            },
            validateField = { field, text -> viewModel.submit(BugReportAction.ValidateField(field, text)) }
        ),
        modifier = modifier
    )
}

@Composable
private fun BugReportScreen(
    state: BugReportState,
    actions: BugReportScreen.Actions,
    modifier: Modifier = Modifier
) {
    var title by remember(state.title) { mutableStateOf(state.title) }
    var description by remember(state.description) { mutableStateOf(state.description) }
    var steps by remember(state.steps) { mutableStateOf(state.steps) }
    var expectedBehavior by remember(state.expectedBehavior) { mutableStateOf(state.expectedBehavior) }

    val titleFocusRequester = remember { FocusRequester() }
    val descriptionFocusRequester = remember { FocusRequester() }
    val stepsFocusRequester = remember { FocusRequester() }
    val expectedBehaviorFocusRequester = remember { FocusRequester() }

    Scaffold(
        modifier = modifier
            .testTag(TestTag.BugReport)
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
            ValidableTextField(
                modifier = Modifier
                    .focusRequester(titleFocusRequester)
                    .focusProperties {
                        next = descriptionFocusRequester
                    },
                state = title,
                onStateChange = { title = it },
                label = TextRes(string.report_title),
                placeholder = TextRes(string.report_report_bug_title_placeholder),
                onFocused = { actions.onFieldFocused(BugReportField.Title) },
                validate = { actions.validateField(BugReportField.Title, title.text) }
            )
            ValidableTextField(
                modifier = Modifier
                    .focusRequester(descriptionFocusRequester)
                    .focusProperties {
                        previous = titleFocusRequester
                        next = stepsFocusRequester
                    },
                state = description,
                onStateChange = { description = it },
                label = TextRes(string.report_description),
                placeholder = TextRes(string.report_report_bug_description_placeholder),
                minLines = 3,
                onFocused = { actions.onFieldFocused(BugReportField.Description) },
                validate = { actions.validateField(BugReportField.Description, description.text) }
            )
            ValidableTextField(
                modifier = Modifier
                    .focusRequester(stepsFocusRequester)
                    .focusProperties {
                        previous = descriptionFocusRequester
                        next = expectedBehaviorFocusRequester
                    },
                state = steps,
                onStateChange = { steps = it },
                label = TextRes(string.report_steps),
                placeholder = TextRes(string.report_steps_placeholder),
                minLines = 3,
                onFocused = { actions.onFieldFocused(BugReportField.Steps) },
                validate = { actions.validateField(BugReportField.Steps, steps.text) }
            )
            ValidableTextField(
                modifier = Modifier
                    .focusRequester(expectedBehaviorFocusRequester)
                    .focusProperties {
                        previous = stepsFocusRequester
                    },
                state = expectedBehavior,
                onStateChange = { expectedBehavior = it },
                label = TextRes(string.report_expected_behavior),
                placeholder = TextRes(string.report_expected_behavior_placeholder),
                minLines = 2,
                onFocused = { actions.onFieldFocused(BugReportField.ExpectedBehavior) },
                validate = { actions.validateField(BugReportField.ExpectedBehavior, expectedBehavior.text) }
            )
            Spacer(modifier = Modifier.padding(top = Dimens.Margin.xxxLarge))
        }
    }

    LaunchedEffect(Unit) { titleFocusRequester.requestFocus() }
}

@Composable
private fun TopBar() {
    CenterAlignedTopAppBar(title = { Text(text = stringResource(id = string.report_report_bug)) })
}

object BugReportScreen {

    data class Actions(
        val back: () -> Unit,
        val onFieldFocused: (field: BugReportField) -> Unit,
        val submit: (description: String, expectedBehavior: String, steps: String, title: String) -> Unit,
        val validateField: (field: BugReportField, text: String) -> Unit
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
private fun BugReportScreenPreview() {
    CineScoutTheme {
        BugReportScreen(
            state = BugReportState.Empty,
            actions = BugReportScreen.Actions.Empty
        )
    }
}
