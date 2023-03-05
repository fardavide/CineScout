package cinescout.account.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.account.domain.model.GetAccountError
import cinescout.account.domain.usecase.GetCurrentAccount
import cinescout.account.presentation.action.ManageAccountAction
import cinescout.account.presentation.mapper.AccountUiModelMapper
import cinescout.account.presentation.state.ManageAccountState
import cinescout.auth.trakt.domain.model.TraktAuthorizationCode
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import cinescout.auth.trakt.domain.usecase.NotifyTraktAppAuthorized
import cinescout.auth.trakt.domain.usecase.UnlinkFromTrakt
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.design.R.string
import cinescout.design.TextRes
import cinescout.design.util.Effect
import cinescout.suggestions.domain.model.SuggestionsMode
import cinescout.suggestions.domain.usecase.StartUpdateSuggestions
import cinescout.utils.android.CineScoutViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ManageAccountViewModel(
    private val accountUiModelMapper: AccountUiModelMapper,
    private val getCurrentAccount: GetCurrentAccount,
    private val linkToTrakt: LinkToTrakt,
    private val notifyTraktAppAuthorized: NotifyTraktAppAuthorized,
    private val networkErrorMapper: NetworkErrorToMessageMapper,
    private val startUpdateSuggestions: StartUpdateSuggestions,
    private val unlinkFromTrakt: UnlinkFromTrakt
) : CineScoutViewModel<ManageAccountAction, ManageAccountState>(ManageAccountState.Loading) {

    init {
        viewModelScope.launch {
            getCurrentAccount().map { accountEither ->
                accountEither.fold(
                    ifLeft = { accountError ->
                        when (accountError) {
                            is GetAccountError.Network -> ManageAccountState.Account.Error(
                                message = networkErrorMapper.toMessage(accountError.networkError)
                            )
                            is GetAccountError.NotConnected -> ManageAccountState.Account.NotConnected
                        }
                    },
                    ifRight = { account ->
                        val uiModel = accountUiModelMapper.toUiModel(account)
                        ManageAccountState.Account.Connected(uiModel)
                    }
                )
            }.collectLatest { account ->
                updateState { currentState ->
                    currentState.copy(account = account)
                }
            }
        }
    }

    override fun submit(action: ManageAccountAction) {
        when (action) {
            ManageAccountAction.LinkToTrakt -> onLinkToTrakt()
            is ManageAccountAction.NotifyTraktAppAuthorized -> onNotifyTraktAppAuthorized(action.code)
            ManageAccountAction.UnlinkFromTrakt -> onUnlinkFromTrakt()
        }
    }

    private fun onLinkToTrakt() {
        viewModelScope.launch {
            linkToTrakt().collectLatest { either ->
                updateState { currentState ->
                    either.fold(
                        ifLeft = {
                            currentState.copy(loginEffect = Effect.of(toLoginState(it))).also { cancel() }
                        },
                        ifRight = { linkState ->
                            currentState.copy(loginEffect = Effect.of(toLoginState(linkState))).also {
                                if (linkState is LinkToTrakt.State.Success) {
                                    startUpdateSuggestions(suggestionsMode = SuggestionsMode.Quick)
                                    cancel()
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    private fun onNotifyTraktAppAuthorized(code: TraktAuthorizationCode) {
        viewModelScope.launch {
            notifyTraktAppAuthorized(code)
        }
    }

    private fun onUnlinkFromTrakt() {
        viewModelScope.launch {
            unlinkFromTrakt()
        }
    }

    private fun toLoginState(state: LinkToTrakt.State): ManageAccountState.Login = when (state) {
        LinkToTrakt.State.Success -> ManageAccountState.Login.Linked
        is LinkToTrakt.State.UserShouldAuthorizeApp ->
            ManageAccountState.Login.UserShouldAuthorizeApp(state.authorizationUrl)
    }

    private fun toLoginState(error: LinkToTrakt.Error): ManageAccountState.Login.Error {
        val message = when (error) {
            is LinkToTrakt.Error.Network -> networkErrorMapper.toMessage(error.networkError)
            LinkToTrakt.Error.UserDidNotAuthorizeApp -> TextRes(string.home_login_app_not_authorized)
        }
        return ManageAccountState.Login.Error(message)
    }
}
