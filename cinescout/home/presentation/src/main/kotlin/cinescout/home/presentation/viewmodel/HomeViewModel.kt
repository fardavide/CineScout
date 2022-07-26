package cinescout.home.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import cinescout.auth.tmdb.domain.usecase.LinkToTmdb
import cinescout.auth.trakt.domain.usecase.LinkToTrakt
import cinescout.home.presentation.model.HomeAction
import cinescout.home.presentation.model.HomeState
import cinescout.utils.android.CineScoutViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val linkToTmdb: LinkToTmdb,
    private val linkToTrakt: LinkToTrakt
) : CineScoutViewModel<HomeAction, HomeState>(initialState = HomeState.Loading) {

    override fun submit(action: HomeAction) {
        when (action) {
            HomeAction.LoginToTmdb -> onLoginToTmdb()
            HomeAction.LoginToTrakt -> onLoginToTrakt()
        }
    }

    private fun onLoginToTmdb() {
        viewModelScope.launch {
            linkToTmdb()
        }
    }

    private fun onLoginToTrakt() {
        viewModelScope.launch {
            linkToTrakt()
        }
    }
}
