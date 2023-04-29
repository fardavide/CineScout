package cinescout.settings.presentation.ui

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import cinescout.design.AdaptivePreviews
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.ui.CineScoutBottomBar
import cinescout.resources.R
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@Composable
fun SettingsScreen(back: () -> Unit) {
    Scaffold(
        modifier = Modifier.testTag(TestTag.ManageAccount),
        topBar = { TopBar() },
        bottomBar = { BottomBar(back = back) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .testTag(TestTag.Settings)
                .padding(paddingValues)
                .padding(Dimens.Margin.Small)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Dimens.Margin.Small)
        ) {
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
        }
    }
}

@Composable
private fun TopBar() {
    CenterAlignedTopAppBar(title = { Text(text = stringResource(id = R.string.settings)) })
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
            text = stringResource(id = R.string.settings_notification_permissions),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(Dimens.Margin.Medium))
        val textRes = when {
            permissionStatus.isGranted -> R.string.settings_notification_permissions_granted
            permissionStatus.shouldShowRationale -> R.string.settings_notification_permissions_hint
            else -> R.string.settings_notification_permissions_hint
        }
        Text(
            text = stringResource(id = textRes),
            style = MaterialTheme.typography.labelLarge
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
                    contentDescription = stringResource(id = R.string.back_button_description)
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
@AdaptivePreviews.WithBackground
private fun SettingsScreenPreview() {
    CineScoutTheme {
        SettingsScreen(back = {})
    }
}
