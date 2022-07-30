package cinescout.home.presentation.viewmodel

import androidx.lifecycle.viewModelScope
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
    private val linkToTmdb: LinkToTmdb,
    private val linkToTrakt: LinkToTrakt,
    private val networkErrorMapper: NetworkErrorToMessageMapper,
    private val notifyTmdbAppAuthorized: NotifyTmdbAppAuthorized,
    private val notifyTraktAppAuthorized: NotifyTraktAppAuthorized
) : CineScoutViewModel<HomeAction, HomeState>(initialState = HomeState.Idle) {

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
                        ifLeft = { currentState.copy(loginStateEffect = Effect.of(toLinkState(it))) },
                        ifRight = { currentState.copy(loginStateEffect = Effect.of(toLinkState(it))) }
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
                        ifLeft = { currentState.copy(loginStateEffect = Effect.of(toLinkState(it))) },
                        ifRight = { currentState.copy(loginStateEffect = Effect.of(toLinkState(it))) }
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

    private fun toLinkState(state: LinkToTmdb.State): HomeState.LoginState = when (state) {
        LinkToTmdb.State.Success -> HomeState.LoginState.Linked
        is LinkToTmdb.State.UserShouldAuthorizeToken -> HomeState.LoginState.UserShouldAuthorizeApp(state.authorizationUrl)
    }

    private fun toLinkState(error: LinkToTmdb.Error): HomeState.LoginState.Error {
        val message = when (error) {
            is LinkToTmdb.Error.Network -> networkErrorMapper.toMessage(error.networkError)
            LinkToTmdb.Error.UserDidNotAuthorizeToken -> TextRes(string.home_login_app_not_authorized)
        }
        return HomeState.LoginState.Error(message)
    }

    private fun toLinkState(state: LinkToTrakt.State): HomeState.LoginState = when (state) {
        LinkToTrakt.State.Success -> HomeState.LoginState.Linked
        is LinkToTrakt.State.UserShouldAuthorizeApp -> HomeState.LoginState.UserShouldAuthorizeApp(state.authorizationUrl)
    }

    private fun toLinkState(error: LinkToTrakt.Error): HomeState.LoginState.Error {
        val message = when (error) {
            is LinkToTrakt.Error.Network -> networkErrorMapper.toMessage(error.networkError)
            LinkToTrakt.Error.UserDidNotAuthorizeApp -> TextRes(string.home_login_app_not_authorized)
        }
        return HomeState.LoginState.Error(message)
    }
}
