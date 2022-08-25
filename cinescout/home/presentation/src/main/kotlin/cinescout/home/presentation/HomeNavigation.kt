package cinescout.home.presentation

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import cinescout.design.Destination
import cinescout.design.TextRes
import kotlinx.coroutines.flow.map
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

@Composable
internal fun NavController.currentHomeDestinationAsState(): State<HomeDestination> =
    currentBackStackEntryFlow
        .map { it.destination.toHomeDestination() }
        .collectAsState(initial = HomeDestination.Start)

internal fun NavController.currentHomeDestination(): HomeDestination =
    currentDestination.toHomeDestination()

private fun NavDestination?.toHomeDestination(): HomeDestination =
    when (this?.route) {
        null -> HomeDestination.Start
        HomeDestination.ForYou.route -> HomeDestination.ForYou
        HomeDestination.Watchlist.route -> HomeDestination.Watchlist
        else -> throw IllegalStateException("Current destination is not a home destination")
    }
