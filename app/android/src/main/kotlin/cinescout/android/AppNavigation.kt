package cinescout.android

import cinescout.design.Destination
import cinescout.details.presentation.ui.MovieDetailsScreen

sealed class AppDestination(path: String, vararg keys: String) : Destination(path, *keys) {

    object About : AppDestination("about")
    object Home : AppDestination("home")
    object MovieDetails : AppDestination("movie_details", MovieDetailsScreen.MovieIdKey)
    object Settings : AppDestination("settings")
    object TvSeriesDetails : AppDestination("tv_series_details")
}
