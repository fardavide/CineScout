package cinescout.database.ext

import cinescout.database.model.DatabaseMovieIds
import cinescout.database.model.DatabasePersonalRating
import cinescout.database.model.DatabaseRecommendation
import cinescout.database.model.DatabaseScreenplayIds
import cinescout.database.model.DatabaseSuggestion
import cinescout.database.model.DatabaseTmdbMovieId
import cinescout.database.model.DatabaseTmdbScreenplayId
import cinescout.database.model.DatabaseTmdbTvShowId
import cinescout.database.model.DatabaseTraktMovieId
import cinescout.database.model.DatabaseTraktScreenplayId
import cinescout.database.model.DatabaseTraktTvShowId
import cinescout.database.model.DatabaseTvShowIds
import cinescout.database.model.DatabaseWatchlist

val DatabasePersonalRating.ids: DatabaseScreenplayIds
    get() = ids(tmdbId, traktId)

val DatabaseRecommendation.ids: DatabaseScreenplayIds
    get() = ids(screenplayTmdbId, screenplayTraktId)

val DatabaseSuggestion.ids: DatabaseScreenplayIds
    get() = ids(tmdbId, traktId)

val DatabaseWatchlist.ids: DatabaseScreenplayIds
    get() = ids(tmdbId, traktId)

private fun ids(tmdb: DatabaseTmdbScreenplayId, trakt: DatabaseTraktScreenplayId): DatabaseScreenplayIds =
    when (tmdb) {
        is DatabaseTmdbMovieId -> DatabaseMovieIds(
            tmdb = tmdb,
            trakt = trakt as DatabaseTraktMovieId
        )
        is DatabaseTmdbTvShowId -> DatabaseTvShowIds(
            tmdb = tmdb,
            trakt = trakt as DatabaseTraktTvShowId
        )
    }
