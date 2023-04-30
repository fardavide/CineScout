package cinescout.network.trakt.model

import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.TraktScreenplayId

enum class TraktQueryType(private val value: String) {

    All("all"),
    Movies("movies"),
    TvShows("shows");

    override fun toString(): String = value
}

fun ScreenplayTypeFilter.toTraktQuery() = when (this) {
    ScreenplayTypeFilter.All -> TraktQueryType.All
    ScreenplayTypeFilter.Movies -> TraktQueryType.Movies
    ScreenplayTypeFilter.TvShows -> TraktQueryType.TvShows
}

fun ScreenplayTypeFilter.toTraktQueryString() = toTraktQuery().toString()

fun TraktScreenplayId.toTraktQuery() = when (this) {
    is TraktScreenplayId.Movie -> TraktQueryType.Movies
    is TraktScreenplayId.TvShow -> TraktQueryType.TvShows
}

fun TraktScreenplayId.toTraktQueryString() = toTraktQuery().toString()
