package cinescout.network.trakt.model

import cinescout.screenplay.domain.model.ScreenplayType
import cinescout.screenplay.domain.model.TraktScreenplayId

enum class TraktQueryType(private val value: String) {

    All("all"),
    Movies("movies"),
    TvShows("shows");

    override fun toString(): String = value
}

fun ScreenplayType.toTraktQuery() = when (this) {
    ScreenplayType.All -> TraktQueryType.All
    ScreenplayType.Movies -> TraktQueryType.Movies
    ScreenplayType.TvShows -> TraktQueryType.TvShows
}

fun ScreenplayType.toTraktQueryString() = toTraktQuery().toString()

fun TraktScreenplayId.toTraktQuery() = when (this) {
    is TraktScreenplayId.Movie -> TraktQueryType.Movies
    is TraktScreenplayId.TvShow -> TraktQueryType.TvShows
}

fun TraktScreenplayId.toTraktQueryString() = toTraktQuery().toString()
