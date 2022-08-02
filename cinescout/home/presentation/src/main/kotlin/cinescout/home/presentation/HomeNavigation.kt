package cinescout.home.presentation

import cinescout.design.Destination

sealed class HomeDestination(id: String) : Destination("home/$id") {

    object ForYou : HomeDestination("for_you")
    object Watchlist : HomeDestination("watchlist")
}
