package cinescout.home.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import cinescout.design.R.drawable
import cinescout.design.R.string
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.theme.imageBackground
import cinescout.design.ui.CenteredProgress
import cinescout.design.ui.CineScoutBottomBar
import cinescout.design.ui.ErrorScreen
import cinescout.home.presentation.model.AccountUiModel
import cinescout.home.presentation.model.ManageAccountState
import cinescout.home.presentation.preview.ManageAccountStatePreviewProvider
import com.skydoves.landscapist.coil.CoilImage

@Composable
internal fun ManageAccountScreen(
    state: ManageAccountState,
    linkActions: ManageAccountScreen.LinkActions,
    back: () -> Unit
) {
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar(back = back) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (state) {
                is ManageAccountState.Connected -> Account(uiModel = state.uiModel, linkActions.unlink)
                is ManageAccountState.Error -> ErrorScreen(text = state.message)
                ManageAccountState.Loading -> CenteredProgress()
                ManageAccountState.NotConnected -> NoAccount(actions = linkActions)
            }
        }
    }
}

@Composable
private fun TopBar() {
    CenterAlignedTopAppBar(title = { Text(text = stringResource(id = string.manage_account)) })
}

@Composable
private fun Account(uiModel: AccountUiModel, unlink: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val connectedAsText = when (uiModel.source) {
            AccountUiModel.Source.Tmdb -> stringResource(id = string.manage_account_connected_to_tmdb_as)
            AccountUiModel.Source.Trakt -> stringResource(id = string.manage_account_connected_to_trakt_as)
        }
        val serviceIcon = when (uiModel.source) {
            AccountUiModel.Source.Tmdb -> drawable.img_tmdb_logo_short
            AccountUiModel.Source.Trakt -> drawable.img_trakt_logo_red_white
        }
        Box(contentAlignment = Alignment.BottomEnd) {
            CoilImage(
                modifier = Modifier
                    .padding(Dimens.Margin.Medium)
                    .size(Dimens.Image.XLarge)
                    .clip(CircleShape)
                    .imageBackground(),
                imageModel = uiModel::imageUrl,
                previewPlaceholder = drawable.ic_user_color
            )
            Image(
                modifier = Modifier
                    .padding(Dimens.Margin.Medium)
                    .size(Dimens.Image.Small)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = CircleShape
                    )
                    .padding(Dimens.Margin.XSmall),
                painter = painterResource(id = serviceIcon),
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.height(Dimens.Margin.Medium))
        Text(text = connectedAsText)
        Spacer(modifier = Modifier.height(Dimens.Margin.Small))
        Text(text = uiModel.username, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(Dimens.Margin.XLarge))
        Button(onClick = unlink) {
            Text(text = stringResource(id = string.manage_account_disconnect))
        }
    }
}

@Composable
private fun NoAccount(actions: ManageAccountScreen.LinkActions) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = string.manage_account_no_account_connected),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(Dimens.Margin.XLarge))
        Row {
            Image(
                modifier = Modifier.size(Dimens.Icon.Large),
                painter = painterResource(id = drawable.img_tmdb_logo_short),
                contentDescription = stringResource(id = string.tmdb_logo_description)
            )
            Spacer(modifier = Modifier.size(Dimens.Margin.Medium))
            OutlinedButton(onClick = actions.linkToTmdb) {
                Text(text = stringResource(id = string.manage_account_connect_to_tmdb))
            }
        }
        Spacer(modifier = Modifier.height(Dimens.Margin.Medium))
        Row {
            Image(
                modifier = Modifier.size(Dimens.Icon.Large),
                painter = painterResource(id = drawable.img_trakt_logo_red_white),
                contentDescription = stringResource(id = string.trakt_logo_description)
            )
            Spacer(modifier = Modifier.size(Dimens.Margin.Medium))
            OutlinedButton(onClick = actions.linkToTrakt) {
                Text(text = stringResource(id = string.manage_account_connect_to_trakt))
            }
        }
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

object ManageAccountScreen {

    data class LinkActions(
        val linkToTmdb: () -> Unit,
        val linkToTrakt: () -> Unit,
        val unlink: () -> Unit
    ) {

        companion object {

            val Empty = LinkActions(
                linkToTmdb = {},
                linkToTrakt = {},
                unlink = {}
            )
        }
    }
}

@Preview
@Composable
private fun ManageAccountScreenPreview(
    @PreviewParameter(ManageAccountStatePreviewProvider::class) state: ManageAccountState
) {
    CineScoutTheme {
        ManageAccountScreen(state, linkActions = ManageAccountScreen.LinkActions.Empty, back = {})
    }
}
