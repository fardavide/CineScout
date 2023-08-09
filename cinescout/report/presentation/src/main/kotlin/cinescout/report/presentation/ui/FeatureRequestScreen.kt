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
import cinescout.report.presentation.action.FeatureRequestAction
import cinescout.report.presentation.model.FeatureRequestField
import cinescout.report.presentation.state.FeatureRequestState
import cinescout.report.presentation.viewmodel.FeatureRequestViewModel
import cinescout.resources.R.drawable
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.utils.compose.Consume
import org.koin.androidx.compose.koinViewModel

@Composable
fun FeatureRequestScreen(back: () -> Unit, modifier: Modifier = Modifier) {
    val viewModel: FeatureRequestViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateLifecycleAware()

    var modalLinks: ReportLinks? by remember { mutableStateOf(null) }
    Consume(state.submitModal) { links ->
        modalLinks = links
    }
    if (modalLinks != null) {
        ReportChooserModal(links = checkNotNull(modalLinks), onDismiss = { modalLinks = null })
    }
    FeatureRequestScreen(
        state = state,
        actions = FeatureRequestScreen.Actions(
            back = back,
            onFieldFocused = { viewModel.submit(FeatureRequestAction.FocusChanged(it)) },
            submit = { alternativeSolutions, description, title ->
                viewModel.submit(
                    FeatureRequestAction.Submit(
                        alternativeSolutions = alternativeSolutions,
                        description = description,
                        title = title
                    )
                )
            },
            validateField = { field, text -> viewModel.submit(FeatureRequestAction.ValidateField(field, text)) }
        ),
        modifier = modifier
    )
}

@Composable
private fun FeatureRequestScreen(
    state: FeatureRequestState,
    actions: FeatureRequestScreen.Actions,
    modifier: Modifier = Modifier
) {
    var title by remember(state.title) { mutableStateOf(state.title) }
    var description by remember(state.description) { mutableStateOf(state.description) }
    var alternativeSolutions by remember(state.alternativeSolutions) { mutableStateOf(state.alternativeSolutions) }
    Scaffold(
        modifier = modifier
            .testTag(TestTag.FeatureRequest)
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
                    actions.submit(alternativeSolutions.text, description.text, title.text)
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
                onFocused = { actions.onFieldFocused(FeatureRequestField.Title) },
                validate = { actions.validateField(FeatureRequestField.Title, title.text) }
            )
            ValidableTextField(
                state = description,
                onStateChange = { description = it },
                label = TextRes(string.report_description),
                minLines = 3,
                onFocused = { actions.onFieldFocused(FeatureRequestField.Description) },
                validate = { actions.validateField(FeatureRequestField.Description, description.text) }
            )
            ValidableTextField(
                state = alternativeSolutions,
                onStateChange = { alternativeSolutions = it },
                label = TextRes(string.report_alternative_solutions),
                minLines = 2,
                onFocused = { actions.onFieldFocused(FeatureRequestField.AlternativeSolutions) },
                validate = {
                    actions.validateField(FeatureRequestField.AlternativeSolutions, alternativeSolutions.text)
                }
            )
            Spacer(modifier = Modifier.padding(top = Dimens.Margin.xxxLarge))
        }
    }
}

@Composable
private fun TopBar() {
    CenterAlignedTopAppBar(title = { Text(text = stringResource(id = string.report_request_feature)) })
}

object FeatureRequestScreen {

    data class Actions(
        val back: () -> Unit,
        val onFieldFocused: (field: FeatureRequestField) -> Unit,
        val submit: (alternativeSolutions: String, description: String, title: String) -> Unit,
        val validateField: (field: FeatureRequestField, text: String) -> Unit
    ) {

        companion object {

            val Empty = Actions(
                back = {},
                onFieldFocused = {},
                submit = { _, _, _ -> },
                validateField = { _, _ -> }
            )
        }
    }
}

@Preview
@Composable
private fun FeatureRequestFieldScreenPreview() {
    CineScoutTheme {
        FeatureRequestScreen(
            state = FeatureRequestState.Empty,
            actions = FeatureRequestScreen.Actions.Empty
        )
    }
}
