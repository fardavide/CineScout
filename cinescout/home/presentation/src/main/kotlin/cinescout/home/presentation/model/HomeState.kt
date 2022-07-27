package cinescout.home.presentation.model

import cinescout.design.TextRes

data class HomeState(
    val tmdb: LinkState,
    val trakt: LinkState
) {

    sealed interface LinkState {
        data class Error(val message: TextRes) : LinkState
        object Idle : LinkState
        object Linked : LinkState
    }

    companion object {

        val Idle = HomeState(tmdb = LinkState.Idle, trakt = LinkState.Idle)
    }
}
