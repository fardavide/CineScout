package client.android.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import client.Screen
import client.android.Get
import client.android.icon
import client.android.title
import client.resource.Strings
import client.viewModel.DrawerViewModel
import client.viewModel.DrawerViewModel.ProfileState
import co.touchlab.kermit.Logger
import dev.chrisbanes.accompanist.coil.CoilImageWithCrossfade
import domain.auth.LinkToTmdb
import entities.right
import studio.forface.cinescout.R

@Composable
fun DrawerContent(getViewModel: Get<DrawerViewModel>, logger: Logger, content: @Composable () -> Unit) {
    val scope = rememberCoroutineScope()
    val viewModel = remember { getViewModel(scope) }

    val profileState by viewModel.profile.collectAsState()
    val linkingStateEither by viewModel.tmdbLinkResult.collectAsState(LinkToTmdb.State.None.right())
    val linkingState = linkingStateEither.rightOrNull()

    logger.i(linkingState.toString(), "Drawer: linkingState")

    Box(modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)) {
        Column {
            DrawerHeader(
                profileState = profileState,
                onLoginClick = viewModel::startLinkingToTmdb
            )
            content()
        }
    }
}

@Composable
private fun DrawerHeader(profileState: ProfileState, onLoginClick: () -> Unit) {
    Row(modifier = Modifier.padding(8.dp)) {

        // Image
        CoilImageWithCrossfade(
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary)
                .padding(16.dp)
                .size(48.dp),
            data = R.drawable.ic_claus_color
        )

        Spacer(Modifier.width(32.dp))

        // Text
        Column(Modifier.clickable(onClick = onLoginClick), verticalArrangement = Arrangement.SpaceEvenly) {

            val (title, subtitle) = when (profileState) {
                ProfileState.LoggedOut -> Strings.GuestTitle to Strings.ClickToTmdbLoginAction
                ProfileState.LoggingIn -> Strings.LoggingInMessage to ""
                is ProfileState.LoggedIn -> profileState.profile.name.s to profileState.profile.username.s
            }
            Text(text = title, style = MaterialTheme.typography.h5)
            Text(text = subtitle)
        }
    }
}

@Composable
fun DrawerItem(screen: Screen, current: Screen, action: () -> Unit) {

    @Composable
    fun Modifier.maybeBackground() =
        if (screen == current)
            background(
                color = MaterialTheme.colors.secondary.copy(alpha = 0.3f),
                shape = MaterialTheme.shapes.medium
            )
        else this

    Row(modifier = Modifier.padding(6.dp).fillMaxWidth()) {
        Row(
            Modifier
            .clickable(onClick = action)
            .maybeBackground()
            .padding(horizontal = 24.dp, vertical = 6.dp)
            .fillMaxWidth()
        ) {
            Image(
                modifier = Modifier.size(36.dp),
                asset = screen.icon,
            )
            Text(
                modifier = Modifier.padding(start = 42.dp).align(Alignment.CenterVertically),
                style = MaterialTheme.typography.h6,
                text = screen.title,
            )
        }
    }
}
