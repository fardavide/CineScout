package cinescout.home.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.GetAppVersion
import cinescout.account.domain.model.GetAccountError
import cinescout.account.domain.model.Gravatar
import cinescout.account.tmdb.domain.usecase.GetTmdbAccount
import cinescout.account.trakt.domain.usecase.GetTraktAccount
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import store.Refresh
import studio.forface.cinescout.design.R.string
import kotlin.time.Duration.Companion.seconds

internal class HomeViewModel(
    private val getAppVersion: GetAppVersion,
    private val getTmdbAccount: GetTmdbAccount,
    private val getTraktAccount: GetTraktAccount,
    private val linkToTmdb: LinkToTmdb,
    private val linkToTrakt: LinkToTrakt,
    private val networkErrorMapper: NetworkErrorToMessageMapper,
    private val notifyTmdbAppAuthorized: NotifyTmdbAppAuthorized,
    private val notifyTraktAppAuthorized: NotifyTraktAppAuthorized
) : CineScoutViewModel<HomeAction, HomeState>(initialState = HomeState.Loading) {

    init {
        updateState { currentState ->
            currentState.copy(appVersion = HomeState.AppVersion.Data(getAppVersion()))
        }
        viewModelScope.launch {
            combine(
                getTmdbAccount(Refresh.WithInterval(3.seconds)),
                getTraktAccount(Refresh.WithInterval(3.seconds))
            ) { tmdbAccountEither, traktAccountEither ->
                val newTmdbAccount = tmdbAccountEither.fold(
                    ifLeft = { error -> toAccountState(error) },
                    ifRight = { account ->
                        HomeState.Accounts.Account.Data(
                            imageUrl = account.gravatar?.getUrl(Gravatar.Size.SMALL),
                            username = account.username.value
                        )
                    }
                )
                val newTraktAccount = traktAccountEither.fold(
                    ifLeft = { error -> toAccountState(error) },
                    ifRight = { account ->
                        HomeState.Accounts.Account.Data(
                            imageUrl = account.gravatar?.getUrl(Gravatar.Size.SMALL),
                            username = account.username.value
                        )
                    }
                )
                val newPrimaryAccount = newTraktAccount as? HomeState.Accounts.Account.Data
                    ?: newTmdbAccount as? HomeState.Accounts.Account.Data
                    ?: HomeState.Accounts.Account.NoAccountConnected
                HomeState.Accounts(
                    primary = newPrimaryAccount,
                    tmdb = newTmdbAccount,
                    trakt = newTraktAccount
                )
            }.collect { accounts ->
                updateState { currentState ->
                    currentState.copy(accounts = accounts)
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

    private fun toAccountState(error: GetAccountError): HomeState.Accounts.Account = when (error) {
        is GetAccountError.Network -> HomeState.Accounts.Account.Error(networkErrorMapper.toMessage(error.networkError))
        GetAccountError.NoAccountConnected -> HomeState.Accounts.Account.NoAccountConnected
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
