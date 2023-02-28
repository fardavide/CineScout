package cinescout.home.presentation

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import cinescout.design.Destination
import cinescout.design.R.string
import cinescout.design.TextRes
import kotlinx.coroutines.flow.map

sealed class HomeDestination(id: String, val label: TextRes) : Destination("home/$id") {

    constructor(id: String, @StringRes label: Int) : this(id, TextRes(label))

    object ForYou : HomeDestination(id = "for_you", label = string.suggestions_for_you)
    object MyLists : HomeDestination(id = "my_lists", label = string.lists_my_lists)
    object None : HomeDestination(id = "none", label = TextRes(""))

    companion object {

        val Start = ForYou
    }
}

@Composable
internal fun NavController.currentHomeDestinationAsState(): State<HomeDestination> = currentBackStackEntryFlow
    .map { it.destination.toHomeDestination() }
    .collectAsState(initial = HomeDestination.Start)

internal fun NavController.currentHomeDestination(): HomeDestination = currentDestination.toHomeDestination()

private fun NavDestination?.toHomeDestination(): HomeDestination = when (this?.route) {
    null -> HomeDestination.Start
    HomeDestination.ForYou.route -> HomeDestination.ForYou
    HomeDestination.MyLists.route -> HomeDestination.MyLists
    HomeDestination.None.route -> HomeDestination.None
    else -> throw IllegalStateException("Current destination is not a home destination: $this")
}
