package cinescout.design

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

open class Destination(private val path: String, vararg keys: String) {
    
    private val argumentsKeys = keys.toList()
    
    val route: String = run {
        var string = path
        for (key in keys) {
            string += "/{$key}"
        }
        string
    }

    internal fun routeWithArgs(vararg argumentsValues: String): String {
        check(argumentsKeys.size == argumentsValues.size) {
            "Arguments keys and values must have the same size"
        }
        var string = path
        for (value in argumentsValues) {
            string += "/$value"
        }
        return string
    }
}

@Composable
fun NavHost(
    navController: NavHostController,
    startDestination: Destination,
    modifier: Modifier = Modifier,
    route: String? = null,
    builder: NavGraphBuilder.() -> Unit
) = NavHost(
    navController = navController,
    startDestination = startDestination.route,
    modifier = modifier,
    route = route,
    builder = builder
)

fun stringNavArgument(name: String, nullable: Boolean = false): NamedNavArgument = navArgument(name) {
    type = NavType.StringType
    this.nullable = nullable
}

inline operator fun <reified T : Any> NavBackStackEntry.get(key: String): T {
    val string = arguments?.getString(key)
        ?: throw IllegalArgumentException("No argument with key $key found")
    return Json.decodeFromString(string)
}

fun NavController.navigate(destination: Destination, builder: NavOptionsBuilder.() -> Unit = {}) {
    if (destination.route != currentDestination?.route) {
        navigate(destination.route, builder = builder)
    }
}

inline fun <reified T : Any> NavController.navigate(
    destination: Destination,
    arg: T,
    noinline builder: NavOptionsBuilder.() -> Unit = {}
) {
    val argString = Json.encodeToString(arg)
    navigate(destination = destination, argString, builder = builder)
}

inline fun <reified T1 : Any, reified T2 : Any> NavController.navigate(
    destination: Destination,
    arg1: T1,
    arg2: T2,
    noinline builder: NavOptionsBuilder.() -> Unit = {}
) {
    val arg1String = Json.encodeToString(arg1)
    val arg2String = Json.encodeToString(arg2)
    navigate(destination = destination, arg1String, arg2String, builder = builder)
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

fun NavGraphBuilder.composable(
    destination: Destination,
    vararg arguments: NamedNavArgument,
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit
) = composable(
    route = destination.route,
    arguments = arguments.toList(),
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

@PublishedApi
internal fun NavController.navigate(
    destination: Destination,
    vararg args: String,
    builder: NavOptionsBuilder.() -> Unit
) {
    val routeWithArgs = destination.routeWithArgs(*args)
    if (routeWithArgs != currentDestination?.route) {
        navigate(routeWithArgs, builder)
    }
}
