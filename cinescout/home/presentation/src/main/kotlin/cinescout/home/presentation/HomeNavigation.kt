package cinescout.home.presentation

import androidx.annotation.StringRes
import androidx.navigation.NavController
import cinescout.design.Destination
import cinescout.design.TextRes
import studio.forface.cinescout.design.R.string

sealed class HomeDestination(id: String, val label: TextRes) : Destination("home/$id") {

    constructor(id: String, @StringRes title: Int) : this(id, TextRes(title))

    object ForYou : HomeDestination(id = "for_you", title = string.home_for_you)
    object Watchlist : HomeDestination(id = "watchlist", title = string.home_watchlist)

    companion object {

        val Start = ForYou
    }
}

internal fun NavController.requireCurrentHomeDestination(): HomeDestination =
    when (currentDestination?.route) {
        null -> HomeDestination.Start
        HomeDestination.ForYou.route -> HomeDestination.ForYou
        HomeDestination.Watchlist.route -> HomeDestination.Watchlist
        else -> throw IllegalStateException("Current destination is not a home destination")
    }
