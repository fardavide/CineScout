package cinescout.network.trakt.model

import cinescout.screenplay.domain.model.ScreenplayType

enum class TraktQueryType(private val value: String) {

    Movies("movies"),
    MoviesAndTvShows("movies,shows"),
    TvShows("shows");

    override fun toString(): String = value
}

fun ScreenplayType.toTraktQuery() = when (this) {
    ScreenplayType.All -> TraktQueryType.MoviesAndTvShows
    ScreenplayType.Movies -> TraktQueryType.Movies
    ScreenplayType.TvShows -> TraktQueryType.TvShows
}

fun ScreenplayType.toTraktQueryString() = toTraktQuery().toString()
