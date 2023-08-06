package cinescout.settings.presentation.ui

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import cinescout.design.TestTag
import cinescout.design.WithBackgroundAdaptivePreviews
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.ui.CineScoutBottomBar
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.resources.string
import cinescout.settings.presentation.action.SettingsAction
import cinescout.settings.presentation.model.SettingsUiModel
import cinescout.settings.presentation.state.SettingsState
import cinescout.settings.presentation.viewmodel.SettingsViewModel
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(back: () -> Unit) {
    val viewModel: SettingsViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateLifecycleAware()
    SettingsScreen(state = state, action = viewModel::submit, back = back)
}

@Composable
internal fun SettingsScreen(
    state: SettingsState,
    action: (SettingsAction) -> Unit,
    back: () -> Unit
) {
    Scaffold(
        modifier = Modifier.testTag(TestTag.ManageAccount),
        topBar = { TopBar() },
        bottomBar = { BottomBar(back = back) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .testTag(TestTag.Settings)
                .scrollable(rememberScrollState(), Orientation.Vertical)
                .padding(paddingValues)
                .padding(Dimens.Margin.small)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Dimens.Margin.small)
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val permission = rememberPostNotificationPermissionState()
                NotificationPermissions(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.large)
                        .clickable(onClick = permission::launchPermissionRequest),
                    permissionStatus = permission.status
                )
            }
            Suggestions(
                suggestions = state.uiModel.suggestions,
                action = action
            )
        }
    }
}

@Composable
private fun TopBar() {
    CenterAlignedTopAppBar(title = { Text(text = stringResource(id = string.settings)) })
}

@Composable
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun NotificationPermissions(permissionStatus: PermissionStatus, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(Dimens.Margin.small),
            text = stringResource(id = string.settings_notification_permissions),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.width(Dimens.Margin.medium))
        val textRes = when {
            permissionStatus.isGranted -> string.settings_notification_permissions_granted
            permissionStatus.shouldShowRationale -> string.settings_notification_permissions_hint
            else -> string.settings_notification_permissions_hint
        }
        Text(
            modifier = Modifier.padding(horizontal = Dimens.Margin.medium),
            text = stringResource(id = textRes),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun Suggestions(suggestions: SettingsUiModel.Suggestions, action: (SettingsAction) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens.Margin.xSmall)) {
        Text(
            modifier = Modifier.padding(Dimens.Margin.small),
            text = stringResource(id = string.settings_suggestions),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.width(Dimens.Margin.medium))
        SuggestionsEntry(
            title = TextRes(string.settings_suggestions_anticipated),
            description = TextRes(string.settings_suggestions_anticipated_description),
            isSuggestionEnabled = suggestions.isAnticipatedEnabled
        ) { action(SettingsAction.UpdateAnticipated(it)) }
        SuggestionsEntry(
            title = TextRes(string.settings_suggestions_in_app_generated),
            description = TextRes(string.settings_suggestions_in_app_generated_description),
            isSuggestionEnabled = suggestions.isInAppGeneratedEnabled
        ) { action(SettingsAction.UpdateInAppGenerated(it)) }
        SuggestionsEntry(
            title = TextRes(string.settings_suggestions_personal),
            description = TextRes(string.settings_suggestions_personal_description),
            isSuggestionEnabled = suggestions.isPersonalSuggestionsEnabled
        ) { action(SettingsAction.UpdatePersonalSuggestions(it)) }
        SuggestionsEntry(
            title = TextRes(string.settings_suggestions_popular),
            description = TextRes(string.settings_suggestions_popular_description),
            isSuggestionEnabled = suggestions.isPopularEnabled
        ) { action(SettingsAction.UpdatePopular(it)) }
        SuggestionsEntry(
            title = TextRes(string.settings_suggestions_recommended),
            description = TextRes(string.settings_suggestions_recommended_description),
            isSuggestionEnabled = suggestions.isRecommendedEnabled
        ) { action(SettingsAction.UpdateRecommended(it)) }
        SuggestionsEntry(
            title = TextRes(string.settings_suggestions_trending),
            description = TextRes(string.settings_suggestions_trending_description),
            isSuggestionEnabled = suggestions.isTrendingEnabled
        ) { action(SettingsAction.UpdateTrending(it)) }
    }
}

@Composable
private fun SuggestionsEntry(
    title: TextRes,
    description: TextRes,
    isSuggestionEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    var isChecked by remember { mutableStateOf(isSuggestionEnabled) }
    val toggle = {
        isChecked = !isChecked
        onToggle(isChecked)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .clickable { toggle() }
            .padding(vertical = Dimens.Margin.xxSmall, horizontal = Dimens.Margin.medium)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = string(textRes = title),
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = string(textRes = description),
                style = MaterialTheme.typography.labelMedium
            )
        }
        Switch(
            checked = isChecked,
            onCheckedChange = { toggle() }
        )
    }
}

@Composable
private fun BottomBar(back: () -> Unit) {
    CineScoutBottomBar(
        icon = {
            IconButton(onClick = back) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = stringResource(id = string.back_button_description)
                )
            }
        }
    )
}

@Composable
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun rememberPostNotificationPermissionState(): PermissionState =
    if (LocalContext.current is Activity) {
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    } else {
        object : PermissionState {
            override val permission: String = Manifest.permission.POST_NOTIFICATIONS
            override val status: PermissionStatus = PermissionStatus.Denied(shouldShowRationale = false)
            override fun launchPermissionRequest() {}
        }
    }

@Composable
@WithBackgroundAdaptivePreviews
private fun SettingsScreenPreview() {
    val state = SettingsState(
        uiModel = SettingsUiModel(
            suggestions = SettingsUiModel.Suggestions(
                isAnticipatedEnabled = true,
                isInAppGeneratedEnabled = true,
                isPersonalSuggestionsEnabled = true,
                isPopularEnabled = false,
                isRecommendedEnabled = true,
                isTrendingEnabled = false
            )
        )
    )
    CineScoutTheme {
        SettingsScreen(state = state, action = {}, back = {})
    }
}
