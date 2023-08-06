package cinescout.account.presentation.ui

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.core.content.getSystemService
import cinescout.account.presentation.action.ManageAccountAction
import cinescout.account.presentation.model.AccountUiModel
import cinescout.account.presentation.preview.ManageAccountStatePreviewProvider
import cinescout.account.presentation.state.ManageAccountState
import cinescout.account.presentation.viewmodel.ManageAccountViewModel
import cinescout.design.PlainAdaptivePreviews
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.theme.imageBackground
import cinescout.design.ui.CenteredProgress
import cinescout.design.ui.CineScoutBottomBar
import cinescout.design.ui.ErrorScreen
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.resources.R.drawable
import cinescout.resources.R.string
import cinescout.resources.string
import cinescout.utils.compose.Adaptive
import cinescout.utils.compose.Consume
import cinescout.utils.compose.WindowWidthSizeClass
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ManageAccountScreen(viewModel: ManageAccountViewModel = koinViewModel(), back: () -> Unit) {
    val state by viewModel.state.collectAsStateLifecycleAware()
    val linkActions = ManageAccountScreen.LinkActions(
        link = { viewModel.submit(ManageAccountAction.LinkToTrakt) },
        unlink = { viewModel.submit(ManageAccountAction.UnlinkFromTrakt) }
    )
    ManageAccountScreen(state = state, linkActions = linkActions, back = back)
}

@Composable
internal fun ManageAccountScreen(
    state: ManageAccountState,
    linkActions: ManageAccountScreen.LinkActions,
    back: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = SnackbarHostState()

    Scaffold(
        modifier = Modifier.testTag(TestTag.ManageAccount),
        topBar = { TopBar() },
        bottomBar = { BottomBar(back = back) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Consume(state.loginEffect) { loginState ->
            when (loginState) {
                is ManageAccountState.Login.Error -> {
                    val message = string(textRes = loginState.message)
                    scope.launch { snackbarHostState.showSnackbar(message) }
                }

                ManageAccountState.Login.Linked -> {
                    val message = stringResource(id = string.manage_account_logged_in)
                    scope.launch { snackbarHostState.showSnackbar(message) }
                }

                is ManageAccountState.Login.UserShouldAuthorizeApp -> {
                    val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(loginState.authorizationUrl))
                    try {
                        context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        val clipboardManager = context.getSystemService<ClipboardManager>()
                        val clipData = ClipData.newPlainText(
                            stringResource(id = string.login_authorization_url_clipboard_label),
                            loginState.authorizationUrl
                        )
                        clipboardManager?.setPrimaryClip(clipData)
                        val message = stringResource(id = string.login_error_cannot_open_browser)
                        scope.launch {
                            snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Long)
                        }
                    }
                }
            }
        }
        Box(modifier = Modifier.padding(paddingValues)) {
            when (state.account) {
                is ManageAccountState.Account.Connected -> Account(
                    uiModel = state.account.uiModel,
                    linkActions = linkActions
                )
                is ManageAccountState.Account.Error -> ErrorScreen(text = state.account.message)
                ManageAccountState.Account.Loading -> CenteredProgress()
                ManageAccountState.Account.NotConnected -> NoAccount(actions = linkActions)
            }
        }
    }
}

@Composable
private fun TopBar() {
    CenterAlignedTopAppBar(title = { Text(text = stringResource(id = string.manage_account)) })
}

@Composable
private fun Account(uiModel: AccountUiModel, linkActions: ManageAccountScreen.LinkActions) {
    Adaptive { windowSizeClass ->
        val content = @Composable { spacing: Dp ->
            Box(contentAlignment = Alignment.BottomEnd) {
                CoilImage(
                    modifier = Modifier
                        .padding(Dimens.Margin.medium)
                        .size(Dimens.Image.xLarge)
                        .clip(CircleShape)
                        .imageBackground(),
                    imageModel = uiModel::imageUrl,
                    previewPlaceholder = drawable.ic_user_color
                )
                Image(
                    modifier = Modifier
                        .padding(Dimens.Margin.medium)
                        .size(Dimens.Image.xSmall)
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = CircleShape
                        )
                        .padding(Dimens.Margin.xSmall),
                    painter = painterResource(id = drawable.img_trakt_logo_red_white),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.height(spacing))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(id = string.manage_account_connected_to_trakt_as))
                Spacer(modifier = Modifier.height(Dimens.Margin.small))
                Text(text = uiModel.username, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(Dimens.Margin.xLarge))
                Button(onClick = linkActions.unlink) {
                    Text(text = stringResource(id = string.manage_account_disconnect))
                }
            }
        }
        when (windowSizeClass.width) {
            WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                content = { content(Dimens.Margin.medium) }
            )

            WindowWidthSizeClass.Expanded -> Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                content = { content(Dimens.Margin.large) }
            )
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
        Spacer(modifier = Modifier.height(Dimens.Margin.xLarge))
        Row {
            Image(
                modifier = Modifier.size(Dimens.Icon.large),
                painter = painterResource(id = drawable.img_trakt_logo_red_white),
                contentDescription = stringResource(id = string.trakt_logo_description)
            )
            Spacer(modifier = Modifier.size(Dimens.Margin.medium))
            OutlinedButton(onClick = actions.link) {
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
        val link: () -> Unit,
        val unlink: () -> Unit
    ) {

        companion object {

            val Empty = LinkActions(
                link = {},
                unlink = {}
            )
        }
    }
}

@Composable
@PlainAdaptivePreviews
private fun ManageAccountScreenPreview(
    @PreviewParameter(ManageAccountStatePreviewProvider::class) state: ManageAccountState
) {
    CineScoutTheme {
        ManageAccountScreen(state, linkActions = ManageAccountScreen.LinkActions.Empty, back = {})
    }
}
