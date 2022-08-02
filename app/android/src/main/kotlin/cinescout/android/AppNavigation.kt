package cinescout.android

import cinescout.design.Destination

sealed class AppDestination(id: String) : Destination(id) {

    object About : AppDestination("about")
    object Home : AppDestination("home")
    object MovieDetails : AppDestination("movie_details")
    object Settings : AppDestination("settings")
    object TvSeriesDetails : AppDestination("tv_series_details")
}
