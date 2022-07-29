package cinescout.home.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import cinescout.auth.tmdb.domain.usecase.NotifyTmdbAppAuthorized
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import cinescout.design.NetworkErrorToMessageMapper
import cinescout.design.TextRes
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
    private val notifyTmdbAppAuthorized: NotifyTmdbAppAuthorized
) : CineScoutViewModel<HomeAction, HomeState>(initialState = HomeState.Idle) {

    override fun submit(action: HomeAction) {
        when (action) {
            HomeAction.LoginToTmdb -> onLoginToTmdb()
            HomeAction.LoginToTrakt -> onLoginToTrakt()
            HomeAction.NotifyTmdbAppAuthorized -> onNotifyTmdbAppAuthorized()
            HomeAction.NotifyTraktAppAuthorized -> TODO()
        }
    }

    private fun onLoginToTmdb() {
        viewModelScope.launch {
            linkToTmdb().collectLatest { either ->
                updateState {
                    either.fold(
                        ifLeft = ::toLinkState,
                        ifRight = ::toLinkState
                    )
                }
            }
        }
    }

    private fun onLoginToTrakt() {
        viewModelScope.launch {
            linkToTrakt().collectLatest { either ->
                updateState {
                    either.fold(
                        ifLeft = ::toLinkState,
                        ifRight = ::toLinkState
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

    private fun toLinkState(state: LinkToTmdb.State): HomeState = when (state) {
        LinkToTmdb.State.Success -> HomeState.Linked
        is LinkToTmdb.State.UserShouldAuthorizeToken -> HomeState.UserShouldAuthorizeApp(state.authorizationUrl)
    }

    private fun toLinkState(error: LinkToTmdb.Error): HomeState.Error {
        val message = when (error) {
            is LinkToTmdb.Error.Network -> networkErrorMapper.toMessage(error.networkError)
            LinkToTmdb.Error.UserDidNotAuthorizeToken -> TextRes(string.home_login_app_not_authorized)
        }
        return HomeState.Error(message)
    }

    private fun toLinkState(state: LinkToTrakt.State): HomeState = when (state) {
        LinkToTrakt.State.Success -> HomeState.Linked
        is LinkToTrakt.State.UserShouldAuthorizeApp -> HomeState.UserShouldAuthorizeApp(state.authorizationUrl)
    }

    private fun toLinkState(error: LinkToTrakt.Error): HomeState.Error {
        val message = when (error) {
            is LinkToTrakt.Error.Network -> networkErrorMapper.toMessage(error.networkError)
            LinkToTrakt.Error.UserDidNotAuthorizeApp -> TextRes(string.home_login_app_not_authorized)
        }
        return HomeState.Error(message)
    }
}
