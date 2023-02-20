package cinescout.android

import cinescout.design.Destination
import cinescout.details.presentation.ui.MovieDetailsScreen
import cinescout.details.presentation.ui.TvShowDetailsScreen

sealed class AppDestination(path: String, vararg keys: String) : Destination(path, *keys) {

    object About : AppDestination("about")
    object Home : AppDestination("home")
    object ManageAccount : AppDestination("manage_account")
    object MovieDetails : AppDestination("movie_details", MovieDetailsScreen.MovieIdKey)
    object Settings : AppDestination("settings")
    object TvShowDetails : AppDestination("tv_show_details", TvShowDetailsScreen.TvShowIdKey)
}
