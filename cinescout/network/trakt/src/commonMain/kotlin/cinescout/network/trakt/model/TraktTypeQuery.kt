package cinescout.network.trakt.model

import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.ids.MovieIds
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import cinescout.screenplay.domain.model.ids.TraktMovieId
import cinescout.screenplay.domain.model.ids.TraktScreenplayId
import cinescout.screenplay.domain.model.ids.TraktTvShowId
import cinescout.screenplay.domain.model.ids.TvShowIds

enum class TraktQueryType(private val value: String) {

    All("all"),
    Movies("movies"),
    TvShows("shows");

    override fun toString(): String = value
}

fun ScreenplayTypeFilter.toTypeQuery() = when (this) {
    ScreenplayTypeFilter.All -> TraktQueryType.All
    ScreenplayTypeFilter.Movies -> TraktQueryType.Movies
    ScreenplayTypeFilter.TvShows -> TraktQueryType.TvShows
}

fun ScreenplayTypeFilter.toTraktTypeQueryString() = toTypeQuery().toString()

fun ScreenplayIds.toTypeQuery() = when (this) {
    is MovieIds -> TraktQueryType.Movies
    is TvShowIds -> TraktQueryType.TvShows
}

fun ScreenplayIds.toTraktTypeQueryString() = toTypeQuery().toString()

fun TraktScreenplayId.toTypeQuery() = when (this) {
    is TraktMovieId -> TraktQueryType.Movies
    is TraktTvShowId -> TraktQueryType.TvShows
}

fun TraktScreenplayId.toTraktTypeQueryString() = toTypeQuery().toString()