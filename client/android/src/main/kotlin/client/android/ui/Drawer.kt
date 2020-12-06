package client.android.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import client.Screen
import client.android.Get
import client.android.icon
import client.android.title
import client.android.util.ThemedPreview
import client.resource.Strings
import client.viewModel.DrawerViewModel
import client.viewModel.DrawerViewModel.ProfileState
import co.touchlab.kermit.Logger
import dev.chrisbanes.accompanist.coil.CoilImage
import domain.auth.Link
import entities.right
import studio.forface.cinescout.R

@Composable
fun DrawerContent(getViewModel: Get<DrawerViewModel>, logger: Logger, content: @Composable () -> Unit) {
    val scope = rememberCoroutineScope()
    val viewModel = remember { getViewModel(scope) }

    val profileState by viewModel.profile.collectAsState()
    val tmdbProfileState by viewModel.tmdbProfile.collectAsState()
    val traktProfileState by viewModel.traktProfile.collectAsState()
    val tmdbLinkingStateEither by viewModel.tmdbLinkResult.collectAsState(Link.State.None.right())
    val tmdbLinkingState = tmdbLinkingStateEither.rightOrNull()
    val traktLinkingStateEither by viewModel.traktLinkResult.collectAsState(Link.State.None.right())
    val traktLinkingState = traktLinkingStateEither.rightOrNull()

    logger.i(tmdbLinkingState.toString(), "Drawer: Tmdb linkingState")
    logger.i(traktLinkingState.toString(), "Drawer: Trakt linkingState")

    Box(modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)) {
        Column {
            DrawerHeader(
                profileState = profileState,
                tmdbProfileState = tmdbProfileState,
                traktProfileState = traktProfileState,
                linkToTmdb = viewModel::startLinkingToTmdb,
                linkToTrakt = viewModel::startLinkingToTrakt
            )
            content()
        }
    }
}

@Composable
private fun DrawerHeader(
    profileState: ProfileState,
    tmdbProfileState: ProfileState,
    traktProfileState: ProfileState,
    linkToTmdb: () -> Unit,
    linkToTrakt: () -> Unit
) {
    Row(modifier = Modifier.padding(8.dp)) {

        // Image
        CoilImage(
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary)
                .padding(16.dp)
                .size(48.dp),
            data = R.drawable.ic_claus_color,
            fadeIn = true
        )

        Spacer(Modifier.width(32.dp))

        var showDialog by remember {
            mutableStateOf(false)
        }

        if (showDialog) {
            ProfileStatusDialog(
                tmdbProfileState = tmdbProfileState,
                traktProfileState = traktProfileState,
                onTmdbLogin = linkToTmdb,
                onTraktLogin = linkToTrakt,
                onDismiss = { showDialog = false })
        }

        // Text
        Column(Modifier.clickable(onClick = { showDialog = true }), verticalArrangement = Arrangement.SpaceEvenly) {

            val (title, subtitle) = when (profileState) {
                ProfileState.LoggedOut -> Strings.GuestTitle to Strings.ClickToLoginAction
                ProfileState.LoggingIn -> Strings.LoggingInMessage to ""
                is ProfileState.LoggedIn -> profileState.profile.name.s to profileState.profile.username.s
            }
            Text(text = title, style = MaterialTheme.typography.h5)
            Text(text = subtitle)
        }
    }
}

@Composable
private fun ProfileStatusDialog(
    tmdbProfileState: ProfileState,
    traktProfileState: ProfileState,
    onTmdbLogin: () -> Unit,
    onTraktLogin: () -> Unit,
    onDismiss: () -> Unit
) {

    Dialog(onDismissRequest = onDismiss) {
        Column(Modifier
            .background(MaterialTheme.colors.surface, MaterialTheme.shapes.medium)
            .fillMaxWidth()
            .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            UserItem(
                profileState = traktProfileState,
                logo = R.drawable.ic_logo_trakt,
                loginActionText = Strings.LoginToTraktAction,
                onClick = onTraktLogin
            )

            UserItem(
                profileState = tmdbProfileState,
                logo = R.drawable.ic_logo_tmdb,
                loginActionText = Strings.LoginToTmdbAction,
                onClick = onTmdbLogin
            )
        }
    }
}

@Composable
private fun UserItem(
    profileState: ProfileState,
    @DrawableRes logo: Int,
    loginActionText: String,
    onClick: () -> Unit
) {
    val isLoggedIn = profileState is ProfileState.LoggedIn
    val borderStroke =
        if (isLoggedIn) BorderStroke(3.dp, MaterialTheme.colors.secondary)
        else BorderStroke(1.dp, MaterialTheme.colors.onSurface)

    Row(
        Modifier
            .fillMaxWidth()
            .border(borderStroke, MaterialTheme.shapes.medium)
            .padding(16.dp)
            .clickable(onClick = { if (profileState is ProfileState.LoggedOut) onClick() })
    ) {
        Image(
            modifier = Modifier.size(96.dp, 48.dp),
            imageVector = vectorResource (id = logo),
            alignment = Alignment.Center
        )
        Text(
            modifier = Modifier.align(Alignment.CenterVertically).fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.subtitle1,
            text = if (isLoggedIn) Strings.LoggedInMessage else loginActionText,
        )
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
                imageVector = screen.icon,
            )
            Text(
                modifier = Modifier.padding(start = 42.dp).align(Alignment.CenterVertically),
                style = MaterialTheme.typography.h6,
                text = screen.title,
            )
        }
    }
}

@Composable
@Preview("Drawer Header")
private fun LightAppContentPreview() {
    ThemedPreview {
        DrawerHeader(
            profileState = ProfileState.LoggingIn,
            tmdbProfileState = ProfileState.LoggingIn,
            traktProfileState = ProfileState.LoggingIn,
            linkToTmdb = {},
            linkToTrakt = {}
        )
    }
}

@Composable
@Preview("Drawer Header dark")
private fun DarkAppContentPreview() {
    ThemedPreview(darkTheme = true) {
        DrawerHeader(
            profileState = ProfileState.LoggingIn,
            tmdbProfileState = ProfileState.LoggingIn,
            traktProfileState = ProfileState.LoggingIn,
            linkToTmdb = {},
            linkToTrakt = {}
        )
    }
}
