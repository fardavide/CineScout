package cinescout.home.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import studio.forface.cinescout.design.R.string

@Composable
internal fun LoginDialog(actions: LoginDialog.Actions) {
    Dialog(onDismissRequest = actions.onDismissRequest) {
        Card {
            Column(modifier = Modifier.padding(Dimens.Margin.Medium), horizontalAlignment = Alignment.End) {
                IconButton(onClick = actions.onDismissRequest) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = string.close_button_description)
                    )
                }
                OutlinedButton(onClick = {
                    actions.onDismissRequest()
                    actions.loginToTmdb()
                }) {
                    Text(text = stringResource(id = string.home_login_to_tmdb))
                }
                OutlinedButton(onClick = {
                    actions.onDismissRequest()
                    actions.loginToTrakt()
                }) {
                    Text(text = stringResource(id = string.home_login_to_trakt))
                }
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

object LoginDialog {

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
private fun LoginDialogPreview() {
    CineScoutTheme {
        LoginDialog(actions = LoginDialog.Actions.Empty)
    }
}
