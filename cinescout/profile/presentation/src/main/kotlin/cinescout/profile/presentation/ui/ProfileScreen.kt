package cinescout.profile.presentation.ui

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import cinescout.design.AdaptivePreviews
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.ui.CenteredProgress
import cinescout.design.ui.FailureImage
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.profile.presentation.preview.ProfilePreviewProvider
import cinescout.profile.presentation.state.ProfileState
import cinescout.profile.presentation.viewmodel.ProfileViewModel
import cinescout.resources.R.drawable
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.resources.string
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.skydoves.landscapist.coil.CoilImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(actions: ProfileScreen.Actions, modifier: Modifier = Modifier) {
    val viewModel: ProfileViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateLifecycleAware()
    ProfileScreen(state = state, actions = actions, modifier = modifier)
}

@Composable
internal fun ProfileScreen(
    state: ProfileState,
    actions: ProfileScreen.Actions,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .testTag(TestTag.Profile)
            .padding(Dimens.Margin.Small)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Dimens.Margin.Small)
    ) {
        Account(
            modifier = Modifier
                .clip(MaterialTheme.shapes.large)
                .clickable(onClick = actions.toManageAccount)
                .padding(vertical = Dimens.Margin.Small, horizontal = Dimens.Margin.Medium),
            account = state.account
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = rememberPostNotificationPermissionState()
            NotificationPermissions(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .clickable(onClick = permission::launchPermissionRequest)
                    .padding(vertical = Dimens.Margin.Small, horizontal = Dimens.Margin.Medium),
                permissionStatus = permission.status
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.Margin.Small),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = stringResource(id = string.app_version, state.appVersion),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun Account(account: ProfileState.Account, modifier: Modifier = Modifier) {
    val imageUrl = when (account) {
        is ProfileState.Account.Connected -> account.uiModel.imageUrl
        else -> null
    }
    val firstLineText = when (account) {
        is ProfileState.Account.Connected -> TextRes(account.uiModel.username)
        is ProfileState.Account.Error -> TextRes(string.profile_account_error)
        ProfileState.Account.Loading -> TextRes(string.profile_account_loading)
        ProfileState.Account.NotConnected -> TextRes(string.profile_account_not_connected)
    }
    val (firstLineStyle, firstLineFontWeight) = when (account) {
        is ProfileState.Account.Connected -> MaterialTheme.typography.titleMedium to FontWeight.Bold
        else -> MaterialTheme.typography.titleSmall to null
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CoilImage(
            modifier = Modifier
                .size(Dimens.Image.Small)
                .clip(CircleShape),
            imageModel = { imageUrl },
            failure = { FailureImage() },
            loading = { CenteredProgress() },
            previewPlaceholder = drawable.ic_user_color
        )
        Spacer(modifier = Modifier.width(Dimens.Margin.Medium))
        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly) {
            Text(
                text = string(textRes = firstLineText),
                style = firstLineStyle,
                fontWeight = firstLineFontWeight
            )
            Text(
                text = stringResource(id = string.profile_manage_account_hint),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun NotificationPermissions(permissionStatus: PermissionStatus, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
    ) {
        Text(
            text = stringResource(id = string.profile_notification_permissions),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(Dimens.Margin.Medium))
        val textRes = when {
            permissionStatus.isGranted -> string.profile_notification_permissions_granted
            permissionStatus.shouldShowRationale -> string.profile_notification_permissions_hint
            else -> string.profile_notification_permissions_hint
        }
        Text(
            text = stringResource(id = textRes),
            style = MaterialTheme.typography.labelLarge
        )
    }
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

object ProfileScreen {

    data class Actions(
        val toManageAccount: () -> Unit
    ) {

        companion object {
            val Empty = Actions(
                toManageAccount = {}
            )
        }
    }
}

@Composable
@AdaptivePreviews.WithBackground
private fun ProfileScreenPreview(@PreviewParameter(ProfilePreviewProvider::class) state: ProfileState) {
    CineScoutTheme {
        ProfileScreen(state = state, actions = ProfileScreen.Actions.Empty)
    }
}
