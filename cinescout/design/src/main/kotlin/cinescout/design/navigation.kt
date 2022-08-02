package cinescout.design

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation

open class Destination(val route: String)

@Composable
fun NavHost(
    navController: NavHostController,
    startDestination: Destination,
    modifier: Modifier = Modifier,
    route: String? = null,
    builder: NavGraphBuilder.() -> Unit
) = androidx.navigation.compose.NavHost(
    navController = navController,
    startDestination = startDestination.route,
    modifier = modifier,
    route = route,
    builder = builder
)

fun NavController.navigate(destination: Destination, navOptions: NavOptions? = null) {
    if (destination.route != currentDestination?.route) {
        navigate(destination.route, navOptions)
    }
}

fun NavGraphBuilder.composable(
    destination: Destination,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit
) = composable(
    route = destination.route,
    arguments = arguments,
    deepLinks = deepLinks,
    content = content
)

fun NavGraphBuilder.navigation(
    startDestination: Destination,
    route: Destination,
    builder: NavGraphBuilder.() -> Unit
) = navigation(
    startDestination = startDestination.route,
    route = route.route,
    builder = builder
)
