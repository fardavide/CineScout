package cinescout.home.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
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
    private val networkErrorMapper: NetworkErrorToMessageMapper
) : CineScoutViewModel<HomeAction, HomeState>(initialState = HomeState.Idle) {

    override fun submit(action: HomeAction) {
        when (action) {
            HomeAction.LoginToTmdb -> onLoginToTmdb()
            HomeAction.LoginToTrakt -> onLoginToTrakt()
        }
    }

    private fun onLoginToTmdb() {
        viewModelScope.launch {
            linkToTmdb().collectLatest { either ->
                updateState { currentState ->
                    either.fold(
                        ifLeft = { error -> currentState.copy(tmdb = error.toLinkState()) },
                        ifRight = { state -> currentState.copy(tmdb = state.toLinkState()) }
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
                        ifLeft = { error -> currentState.copy(trakt = error.toLinkState()) },
                        ifRight = { state -> currentState.copy(trakt = state.toLinkState()) }
                    )
                }
            }
        }
    }

    private fun LinkToTmdb.Error.toLinkState(): HomeState.LinkState.Error {
        val message = when (this) {
            is LinkToTmdb.Error.Network -> networkErrorMapper.toMessage(networkError)
            LinkToTmdb.Error.UserDidNotAuthorizeToken -> TextRes(string.home_login_app_not_authorized)
        }
        return HomeState.LinkState.Error(message)
    }

    private fun LinkToTmdb.State.toLinkState(): HomeState.LinkState = when (this) {
        LinkToTmdb.State.Success -> HomeState.LinkState.Linked
        is LinkToTmdb.State.UserShouldAuthorizeToken -> TODO()
    }

    private fun LinkToTrakt.Error.toLinkState(): HomeState.LinkState.Error {
        val message = when (this) {
            is LinkToTrakt.Error.Network -> networkErrorMapper.toMessage(networkError)
            LinkToTrakt.Error.UserDidNotAuthorizeApp -> TextRes(string.home_login_app_not_authorized)
        }
        return HomeState.LinkState.Error(message)
    }

    private fun LinkToTrakt.State.toLinkState(): HomeState.LinkState = when (this) {
        LinkToTrakt.State.Success -> HomeState.LinkState.Linked
        is LinkToTrakt.State.UserShouldAuthorizeApp -> TODO()
    }
}
