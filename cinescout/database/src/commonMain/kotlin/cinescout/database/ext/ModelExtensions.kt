package cinescout.database.ext

import cinescout.database.model.DatabasePersonalRating
import cinescout.database.model.DatabaseRecommendation
import cinescout.database.model.DatabaseSuggestion
import cinescout.database.model.DatabaseWatchlist
import cinescout.database.model.id.DatabaseMovieIds
import cinescout.database.model.id.DatabaseScreenplayIds
import cinescout.database.model.id.DatabaseTmdbMovieId
import cinescout.database.model.id.DatabaseTmdbScreenplayId
import cinescout.database.model.id.DatabaseTmdbTvShowId
import cinescout.database.model.id.DatabaseTraktMovieId
import cinescout.database.model.id.DatabaseTraktScreenplayId
import cinescout.database.model.id.DatabaseTraktTvShowId
import cinescout.database.model.id.DatabaseTvShowIds

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
