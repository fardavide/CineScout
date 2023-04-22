package cinescout.home.presentation

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import cinescout.design.Destination
import cinescout.resources.R.string
import cinescout.resources.TextRes

sealed class HomeDestination(id: String, val label: TextRes) : Destination("home/$id") {

    constructor(id: String, @StringRes label: Int) : this(id, TextRes(label))

    object ForYou : HomeDestination(id = "for_you", label = string.suggestions_for_you)
    object MyLists : HomeDestination(id = "my_lists", label = string.lists_my_lists)
    object Profile : HomeDestination(id = "profile", label = string.profile)
    object Search : HomeDestination(id = "search", label = string.search)

    object None : HomeDestination(id = "none", label = TextRes(""))

    companion object {

        val Start = ForYou
    }
}

@Composable
internal fun NavController.currentHomeDestinationAsState(): State<HomeDestination> {
    val entry by currentBackStackEntryFlow.collectAsState(initial = null)
    return remember(entry) {
        derivedStateOf { entry?.destination.toHomeDestination() }
    }
}

private fun NavDestination?.toHomeDestination(): HomeDestination = when (this?.route) {
    null -> HomeDestination.Start
    HomeDestination.ForYou.route -> HomeDestination.ForYou
    HomeDestination.MyLists.route -> HomeDestination.MyLists
    HomeDestination.None.route -> HomeDestination.None
    HomeDestination.Profile.route -> HomeDestination.Profile
    HomeDestination.Search.route -> HomeDestination.Search
    else -> throw IllegalStateException("Current destination is not a home destination: $this")
}
