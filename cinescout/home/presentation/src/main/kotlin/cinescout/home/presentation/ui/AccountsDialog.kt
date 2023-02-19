package cinescout.home.presentation.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.window.Dialog
import cinescout.account.domain.model.Gravatar
import cinescout.account.tmdb.domain.sample.TmdbAccountSample
import cinescout.account.tmdb.domain.sample.TmdbAccountUsernameSample
import cinescout.account.trakt.domain.sample.TraktAccountSample
import cinescout.design.R.drawable
import cinescout.design.R.string
import cinescout.design.TextRes
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.home.presentation.model.HomeState

@Composable
internal fun AccountsDialog(state: HomeState.Accounts, actions: AccountsDialog.Actions) {
    Dialog(onDismissRequest = actions.onDismissRequest) {
        Card {
            Column(
                modifier = Modifier.padding(Dimens.Margin.Medium),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(string.home_manage_accounts),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.size(Dimens.Margin.Medium))
                    IconButton(onClick = actions.onDismissRequest) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(id = string.close_button_description)
                        )
                    }
                }
                Spacer(modifier = Modifier.size(Dimens.Margin.Medium))
                AccountEntry(
                    state = state.tmdb,
                    imageLogo = drawable.img_tmdb_logo_short,
                    imageContentDescription = string.tmdb_logo_description,
                    connectedText = string.home_connected_to_tmdb_as,
                    notConnectedText = string.home_connect_to_tmdb,
                    login = {
                        actions.loginToTmdb()
                        actions.onDismissRequest()
                    }
                )
                Spacer(modifier = Modifier.size(Dimens.Margin.Medium))
                AccountEntry(
                    state = state.trakt,
                    imageLogo = drawable.img_trakt_logo_red_white,
                    imageContentDescription = string.trakt_logo_description,
                    connectedText = string.home_connected_to_trakt_as,
                    notConnectedText = string.home_connect_to_trakt,
                    login = {
                        actions.loginToTrakt()
                        actions.onDismissRequest()
                    }
                )
            }
        }
    }
}

@Composable
private fun AccountEntry(
    state: HomeState.Accounts.Account,
    @DrawableRes imageLogo: Int,
    @StringRes imageContentDescription: Int,
    @StringRes connectedText: Int,
    @StringRes notConnectedText: Int,
    login: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            modifier = Modifier.size(Dimens.Icon.Large),
            painter = painterResource(id = imageLogo),
            contentDescription = stringResource(id = imageContentDescription)
        )
        Spacer(modifier = Modifier.size(Dimens.Margin.Medium))
        when (state) {
            is HomeState.Accounts.Account.Data -> Button(onClick = {}, enabled = false) {
                Text(text = stringResource(id = connectedText, state.username))
            }
            is HomeState.Accounts.Account.Error,
            HomeState.Accounts.Account.Loading,
            HomeState.Accounts.Account.NoAccountConnected -> OutlinedButton(onClick = login) {
                Text(text = stringResource(id = notConnectedText))
            }
        }
    }
}

data class LoginActions(
    val loginToTmdb: () -> Unit,
    val loginToTrakt: () -> Unit
) {

    companion object {

        val Empty = LoginActions(
            loginToTmdb = {},
            loginToTrakt = {}
        )
    }
}

object AccountsDialog {

    data class Actions(
        val loginActions: LoginActions,
        val onDismissRequest: () -> Unit
    ) {

        val loginToTmdb = loginActions.loginToTmdb
        val loginToTrakt = loginActions.loginToTrakt

        companion object {

            val Empty = Actions(
                loginActions = LoginActions.Empty,
                onDismissRequest = {}
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun AccountsDialogPreview(
    @PreviewParameter(AccountsDialogPreviewParameterProvider::class) state: HomeState.Accounts
) {
    CineScoutTheme {
        AccountsDialog(state = state, actions = AccountsDialog.Actions.Empty)
    }
}

private class AccountsDialogPreviewParameterProvider : PreviewParameterProvider<HomeState.Accounts> {
    override val values = sequenceOf(
        HomeState.Accounts(
            primary = HomeState.Accounts.Account.Data(
                username = TraktAccountSample.Username.value,
                TraktAccountSample.Account.gravatar?.getUrl(Gravatar.Size.SMALL)
            ),
            tmdb = HomeState.Accounts.Account.Data(
                username = TmdbAccountUsernameSample.Username.value,
                TmdbAccountSample.Account.gravatar?.getUrl(Gravatar.Size.SMALL)
            ),
            trakt = HomeState.Accounts.Account.Data(
                username = TraktAccountSample.Username.value,
                TraktAccountSample.Account.gravatar?.getUrl(Gravatar.Size.SMALL)
            )
        ),
        HomeState.Accounts(
            primary = HomeState.Accounts.Account.Error(message = TextRes("Error")),
            tmdb = HomeState.Accounts.Account.Error(message = TextRes("Error")),
            trakt = HomeState.Accounts.Account.Error(message = TextRes("Error"))
        ),
        HomeState.Accounts.Loading,
        HomeState.Accounts.NoAccountConnected
    )
}
