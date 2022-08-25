package cinescout.home.presentation

import androidx.annotation.StringRes
import androidx.navigation.NavController
import cinescout.design.Destination
import cinescout.design.TextRes
import studio.forface.cinescout.design.R.string

sealed class HomeDestination(id: String, val label: TextRes) : Destination("home/$id") {

    constructor(id: String, @StringRes label: Int) : this(id, TextRes(label))

    object ForYou : HomeDestination(id = "for_you", label = string.suggestions_for_you)
    object None : HomeDestination(id = "none", label = TextRes(""))
    object Watchlist : HomeDestination(id = "watchlist", label = string.lists_watchlist)

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
