package cinescout.account.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.account.domain.model.GetAccountError
import cinescout.account.domain.usecase.GetCurrentAccount
import cinescout.account.presentation.action.ManageAccountAction
import cinescout.account.presentation.mapper.AccountUiModelMapper
import cinescout.account.presentation.state.ManageAccountState
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import cinescout.auth.tmdb.domain.usecase.NotifyTmdbAppAuthorized
import cinescout.auth.tmdb.domain.usecase.UnlinkFromTmdb
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
    private val linkToTmdb: LinkToTmdb,
    private val linkToTrakt: LinkToTrakt,
    private val notifyTmdbAppAuthorized: NotifyTmdbAppAuthorized,
    private val notifyTraktAppAuthorized: NotifyTraktAppAuthorized,
    private val networkErrorMapper: NetworkErrorToMessageMapper,
    private val startUpdateSuggestions: StartUpdateSuggestions,
    private val unlinkFromTmdb: UnlinkFromTmdb,
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
            ManageAccountAction.LinkToTmdb -> onLinkToTmdb()
            ManageAccountAction.LinkToTrakt -> onLinkToTrakt()
            ManageAccountAction.NotifyTmdbAppAuthorized -> onNotifyTmdbAppAuthorized()
            is ManageAccountAction.NotifyTraktAppAuthorized -> onNotifyTraktAppAuthorized(action.code)
            ManageAccountAction.UnlinkFromTmdb -> onUnlinkFromTmdb()
            ManageAccountAction.UnlinkFromTrakt -> onUnlinkFromTrakt()
        }
    }

    private fun onLinkToTmdb() {
        viewModelScope.launch {
            linkToTmdb().collectLatest { either ->
                updateState { currentState ->
                    either.fold(
                        ifLeft = {
                            currentState.copy(loginEffect = Effect.of(toLoginState(it)))
                                .also { cancel() }
                        },
                        ifRight = { linkState ->
                            currentState.copy(loginEffect = Effect.of(toLoginState(linkState))).also {
                                if (linkState is LinkToTmdb.State.Success) {
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

    private fun onNotifyTmdbAppAuthorized() {
        viewModelScope.launch {
            notifyTmdbAppAuthorized()
        }
    }

    private fun onNotifyTraktAppAuthorized(code: TraktAuthorizationCode) {
        viewModelScope.launch {
            notifyTraktAppAuthorized(code)
        }
    }

    private fun onUnlinkFromTmdb() {
        viewModelScope.launch {
            unlinkFromTmdb()
        }
    }

    private fun onUnlinkFromTrakt() {
        viewModelScope.launch {
            unlinkFromTrakt()
        }
    }

    private fun toLoginState(state: LinkToTmdb.State): ManageAccountState.Login = when (state) {
        LinkToTmdb.State.Success -> ManageAccountState.Login.Linked
        is LinkToTmdb.State.UserShouldAuthorizeToken ->
            ManageAccountState.Login.UserShouldAuthorizeApp(state.authorizationUrl)
    }

    private fun toLoginState(error: LinkToTmdb.Error): ManageAccountState.Login.Error {
        val message = when (error) {
            is LinkToTmdb.Error.Network -> networkErrorMapper.toMessage(error.networkError)
            LinkToTmdb.Error.UserDidNotAuthorizeToken -> TextRes(string.home_login_app_not_authorized)
        }
        return ManageAccountState.Login.Error(message)
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
