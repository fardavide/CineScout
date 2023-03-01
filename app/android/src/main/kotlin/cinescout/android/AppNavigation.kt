package cinescout.android

import cinescout.design.Destination
import cinescout.details.presentation.ui.MovieDetailsScreen
import cinescout.details.presentation.ui.TvShowDetailsScreen

sealed class AppDestination(path: String, vararg keys: String) : Destination(path, *keys) {

    object Home : AppDestination("home")
    object ManageAccount : AppDestination("manage_account")
    object MovieDetails : AppDestination("movie_details", MovieDetailsScreen.MovieIdKey)
    object Profile : AppDestination("profile")
    object TvShowDetails : AppDestination("tv_show_details", TvShowDetailsScreen.TvShowIdKey)
}
