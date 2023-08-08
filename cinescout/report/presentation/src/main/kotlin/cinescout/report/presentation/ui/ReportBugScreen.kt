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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import cinescout.report.presentation.action.ReportBugAction
import cinescout.report.presentation.model.ReportBugField
import cinescout.report.presentation.state.ReportBugState
import cinescout.report.presentation.viewmodel.ReportBugViewModel
import cinescout.resources.R.drawable
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.utils.compose.Consume
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReportBugScreen(back: () -> Unit, modifier: Modifier = Modifier) {
    val viewModel: ReportBugViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateLifecycleAware()

    var modalLinks: ReportLinks? by remember { mutableStateOf(null) }
    Consume(state.submitModal) { links ->
        modalLinks = links
    }
    if (modalLinks != null) {
        ReportChooserModal(links = checkNotNull(modalLinks), onDismiss = { modalLinks = null })
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
            ValidableTextField(
                state = title,
                onStateChange = { title = it },
                label = TextRes(string.report_title),
                onFocused = { actions.onFieldFocused(ReportBugField.Title) },
                validate = { actions.validateField(ReportBugField.Title, title.text) }
            )
            ValidableTextField(
                state = description,
                onStateChange = { description = it },
                label = TextRes(string.report_description),
                minLines = 3,
                onFocused = { actions.onFieldFocused(ReportBugField.Description) },
                validate = { actions.validateField(ReportBugField.Description, description.text) }
            )
            ValidableTextField(
                state = steps,
                onStateChange = { steps = it },
                label = TextRes(string.report_steps),
                minLines = 2,
                onFocused = { actions.onFieldFocused(ReportBugField.Steps) },
                validate = { actions.validateField(ReportBugField.Steps, steps.text) }
            )
            ValidableTextField(
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
