package cinescout.home.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.account.tmdb.domain.model.GetAccountError
import cinescout.account.tmdb.domain.usecase.GetTmdbAccount
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import cinescout.auth.tmdb.domain.usecase.NotifyTmdbAppAuthorized
import cinescout.auth.trakt.domain.model.TraktAuthorizationCode
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import cinescout.auth.trakt.domain.usecase.NotifyTraktAppAuthorized
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.design.TextRes
import cinescout.design.util.Effect
import cinescout.home.presentation.model.HomeAction
import cinescout.home.presentation.model.HomeState
import cinescout.utils.android.CineScoutViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import studio.forface.cinescout.design.R.string

class HomeViewModel(
    private val getTmdbAccount: GetTmdbAccount,
    private val linkToTmdb: LinkToTmdb,
    private val linkToTrakt: LinkToTrakt,
    private val networkErrorMapper: NetworkErrorToMessageMapper,
    private val notifyTmdbAppAuthorized: NotifyTmdbAppAuthorized,
    private val notifyTraktAppAuthorized: NotifyTraktAppAuthorized
) : CineScoutViewModel<HomeAction, HomeState>(initialState = HomeState.Idle) {

    init {
        viewModelScope.launch {
            getTmdbAccount().collectLatest { either ->
                updateState { currentState ->
                    either.fold(
                        ifLeft = { error -> currentState.copy(account = toAccountState(error)) },
                        ifRight = { account -> currentState.copy(account = HomeState.Account.Data(account)) }
                    )
                }
            }
        }
    }

    override fun submit(action: HomeAction) {
        when (action) {
            HomeAction.LoginToTmdb -> onLoginToTmdb()
            HomeAction.LoginToTrakt -> onLoginToTrakt()
            HomeAction.NotifyTmdbAppAuthorized -> onNotifyTmdbAppAuthorized()
            is HomeAction.NotifyTraktAppAuthorized -> onNotifyTraktAppAuthorized(action.code)
        }
    }

    private fun onLoginToTmdb() {
        viewModelScope.launch {
            linkToTmdb().collectLatest { either ->
                updateState { currentState ->
                    either.fold(
                        ifLeft = { currentState.copy(loginEffect = Effect.of(toLoginState(it))) },
                        ifRight = { currentState.copy(loginEffect = Effect.of(toLoginState(it))) }
                    )
                }
            }
        }
    }

    private fun onLoginToTrakt() {
        viewModelScope.launch {
            linkToTrakt().collectLatest { either ->
                updateState { currentState ->
                    either.fold(
                        ifLeft = { currentState.copy(loginEffect = Effect.of(toLoginState(it))) },
                        ifRight = { currentState.copy(loginEffect = Effect.of(toLoginState(it))) }
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

    private fun toAccountState(error: GetAccountError): HomeState.Account = when (error) {
        is GetAccountError.Network -> HomeState.Account.Error(networkErrorMapper.toMessage(error.networkError))
        GetAccountError.NoAccountConnected -> HomeState.Account.NoAccountConnected
    }

    private fun toLoginState(state: LinkToTmdb.State): HomeState.Login = when (state) {
        LinkToTmdb.State.Success -> HomeState.Login.Linked
        is LinkToTmdb.State.UserShouldAuthorizeToken -> HomeState.Login.UserShouldAuthorizeApp(state.authorizationUrl)
    }

    private fun toLoginState(error: LinkToTmdb.Error): HomeState.Login.Error {
        val message = when (error) {
            is LinkToTmdb.Error.Network -> networkErrorMapper.toMessage(error.networkError)
            LinkToTmdb.Error.UserDidNotAuthorizeToken -> TextRes(string.home_login_app_not_authorized)
        }
        return HomeState.Login.Error(message)
    }

    private fun toLoginState(state: LinkToTrakt.State): HomeState.Login = when (state) {
        LinkToTrakt.State.Success -> HomeState.Login.Linked
        is LinkToTrakt.State.UserShouldAuthorizeApp -> HomeState.Login.UserShouldAuthorizeApp(state.authorizationUrl)
    }

    private fun toLoginState(error: LinkToTrakt.Error): HomeState.Login.Error {
        val message = when (error) {
            is LinkToTrakt.Error.Network -> networkErrorMapper.toMessage(error.networkError)
            LinkToTrakt.Error.UserDidNotAuthorizeApp -> TextRes(string.home_login_app_not_authorized)
        }
        return HomeState.Login.Error(message)
    }
}
