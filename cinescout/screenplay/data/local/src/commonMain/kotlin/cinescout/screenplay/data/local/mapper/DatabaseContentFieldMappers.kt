@file:Suppress("TooManyFunctions")

package cinescout.screenplay.data.local.mapper

import cinescout.database.model.DatabaseTvShowStatus
import cinescout.database.model.id.DatabaseGenreSlug
import cinescout.database.model.id.DatabaseMovieIds
import cinescout.database.model.id.DatabaseScreenplayIds
import cinescout.database.model.id.DatabaseTmdbEpisodeId
import cinescout.database.model.id.DatabaseTmdbKeywordId
import cinescout.database.model.id.DatabaseTmdbMovieId
import cinescout.database.model.id.DatabaseTmdbScreenplayId
import cinescout.database.model.id.DatabaseTmdbTvShowId
import cinescout.database.model.id.DatabaseTraktEpisodeId
import cinescout.database.model.id.DatabaseTraktMovieId
import cinescout.database.model.id.DatabaseTraktScreenplayId
import cinescout.database.model.id.DatabaseTraktTvShowId
import cinescout.database.model.id.DatabaseTvShowIds
import cinescout.screenplay.domain.model.TmdbKeywordId
import cinescout.screenplay.domain.model.TvShowStatus
import cinescout.screenplay.domain.model.id.EpisodeIds
import cinescout.screenplay.domain.model.id.GenreSlug
import cinescout.screenplay.domain.model.id.MovieIds
import cinescout.screenplay.domain.model.id.ScreenplayIds
import cinescout.screenplay.domain.model.id.TmdbMovieId
import cinescout.screenplay.domain.model.id.TmdbScreenplayId
import cinescout.screenplay.domain.model.id.TmdbTvShowId
import cinescout.screenplay.domain.model.id.TraktMovieId
import cinescout.screenplay.domain.model.id.TraktScreenplayId
import cinescout.screenplay.domain.model.id.TraktTvShowId
import cinescout.screenplay.domain.model.id.TvShowIds

fun ScreenplayIds.toTmdbDatabaseId() = tmdb.toDatabaseId()
fun ScreenplayIds.toTraktDatabaseId() = trakt.toDatabaseId()
fun TmdbKeywordId.toDatabaseId() = DatabaseTmdbKeywordId(value)
fun GenreSlug.toDatabaseId() = DatabaseGenreSlug(value)
fun TmdbScreenplayId.toDatabaseId(): DatabaseTmdbScreenplayId = when (this) {
    is TmdbMovieId -> DatabaseTmdbMovieId(value)
    is TmdbTvShowId -> DatabaseTmdbTvShowId(value)
}

fun TmdbScreenplayId.toStringDatabaseId() = value.toString()
fun TmdbMovieId.toDatabaseId() = DatabaseTmdbMovieId(value)
fun TmdbTvShowId.toDatabaseId() = DatabaseTmdbTvShowId(value)
fun TraktScreenplayId.toDatabaseId(): DatabaseTraktScreenplayId = when (this) {
    is TraktMovieId -> DatabaseTraktMovieId(value)
    is TraktTvShowId -> DatabaseTraktTvShowId(value)
}

fun EpisodeIds.toTmdbDatabaseId() = DatabaseTmdbEpisodeId(tmdb.value)
fun EpisodeIds.toTraktDatabaseId() = DatabaseTraktEpisodeId(trakt.value)
fun MovieIds.toTmdbDatabaseId() = DatabaseTmdbMovieId(tmdb.value)
fun MovieIds.toTraktDatabaseId() = DatabaseTraktMovieId(trakt.value)
fun TraktMovieId.toDatabaseId() = DatabaseTraktMovieId(value)
fun TraktTvShowId.toDatabaseId() = DatabaseTraktTvShowId(value)
fun TraktScreenplayId.toStringDatabaseId() = value.toString()
fun TvShowIds.toTmdbDatabaseId() = DatabaseTmdbTvShowId(tmdb.value)
fun TvShowIds.toTraktDatabaseId() = DatabaseTraktTvShowId(value = trakt.value)
fun TvShowStatus.toDatabaseStatus() = when (this) {
    TvShowStatus.Canceled -> DatabaseTvShowStatus.Canceled
    TvShowStatus.Continuing -> DatabaseTvShowStatus.Continuing
    TvShowStatus.Ended -> DatabaseTvShowStatus.Ended
    TvShowStatus.InProduction -> DatabaseTvShowStatus.InProduction
    TvShowStatus.Pilot -> DatabaseTvShowStatus.Pilot
    TvShowStatus.Planned -> DatabaseTvShowStatus.Planned
    TvShowStatus.ReturningSeries -> DatabaseTvShowStatus.ReturningSeries
    TvShowStatus.Rumored -> DatabaseTvShowStatus.Rumored
    TvShowStatus.Upcoming -> DatabaseTvShowStatus.Upcoming
}

fun toScreenplayIds(tmdb: DatabaseTmdbScreenplayId, trakt: DatabaseTraktScreenplayId) = ScreenplayIds(
    tmdb = tmdb.toDomainId(),
    trakt = trakt.toDomainId()
)
fun toTvShowIds(tmdbId: DatabaseTmdbTvShowId, traktId: DatabaseTraktTvShowId) = TvShowIds(
    tmdb = tmdbId.toTvShowDomainId(),
    trakt = traktId.toTvShowDomainId()
)

fun DatabaseGenreSlug.toDomainId() = GenreSlug(value)
fun DatabaseScreenplayIds.toDomainIds() = when (this) {
    is DatabaseMovieIds -> MovieIds(
        tmdb = TmdbMovieId(tmdb.value),
        trakt = TraktMovieId(trakt.value)
    )

    is DatabaseTvShowIds -> TvShowIds(
        tmdb = TmdbTvShowId(tmdb.value),
        trakt = TraktTvShowId(trakt.value)
    )
}

fun DatabaseScreenplayIds.toMovieDomainIds() = MovieIds(
    tmdb = TmdbMovieId(tmdb.value),
    trakt = TraktMovieId(trakt.value)
)

fun DatabaseScreenplayIds.toTvShowDomainIds() = TvShowIds(
    tmdb = TmdbTvShowId(tmdb.value),
    trakt = TraktTvShowId(trakt.value)
)

fun DatabaseTmdbKeywordId.toDomainId() = TmdbKeywordId(value)
fun DatabaseTmdbScreenplayId.toDomainId(): TmdbScreenplayId = when (this) {
    is DatabaseTmdbMovieId -> TmdbMovieId(value)
    is DatabaseTmdbTvShowId -> TmdbTvShowId(value)
}

fun DatabaseTmdbScreenplayId.toMovieDomainId(): TmdbMovieId = when (this) {
    is DatabaseTmdbMovieId -> TmdbMovieId(value)
    is DatabaseTmdbTvShowId -> error("Expected a movie id, but got a tv show id: $this")
}

fun DatabaseTmdbScreenplayId.toTvShowDomainId(): TmdbTvShowId = when (this) {
    is DatabaseTmdbMovieId -> error("Expected a tv show id, but got a movie id: $this")
    is DatabaseTmdbTvShowId -> TmdbTvShowId(value)
}

fun DatabaseTraktScreenplayId.toMovieDomainId(): TraktMovieId = when (this) {
    is DatabaseTraktMovieId -> TraktMovieId(value)
    is DatabaseTraktTvShowId -> error("Expected a movie id, but got a tv show id: $this")
}

fun DatabaseTraktScreenplayId.toDomainId(): TraktScreenplayId = when (this) {
    is DatabaseTraktMovieId -> TraktMovieId(value)
    is DatabaseTraktTvShowId -> TraktTvShowId(value)
}

fun DatabaseTraktScreenplayId.toTvShowDomainId(): TraktTvShowId = when (this) {
    is DatabaseTraktMovieId -> error("Expected a tv show id, but got a movie id: $this")
    is DatabaseTraktTvShowId -> TraktTvShowId(value)
}

fun DatabaseTvShowStatus.toTvShowStatus(): TvShowStatus = when (this) {
    DatabaseTvShowStatus.Canceled -> TvShowStatus.Canceled
    DatabaseTvShowStatus.Continuing -> TvShowStatus.Continuing
    DatabaseTvShowStatus.Ended -> TvShowStatus.Ended
    DatabaseTvShowStatus.InProduction -> TvShowStatus.InProduction
    DatabaseTvShowStatus.Pilot -> TvShowStatus.Pilot
    DatabaseTvShowStatus.Planned -> TvShowStatus.Planned
    DatabaseTvShowStatus.ReturningSeries -> TvShowStatus.ReturningSeries
    DatabaseTvShowStatus.Rumored -> TvShowStatus.Rumored
    DatabaseTvShowStatus.Upcoming -> TvShowStatus.Upcoming
}
