package cinescout.screenplay.data.local.mapper

import cinescout.database.model.id.DatabaseMovieIds
import cinescout.database.model.id.DatabaseScreenplayIds
import cinescout.database.model.id.DatabaseTmdbGenreId
import cinescout.database.model.id.DatabaseTmdbKeywordId
import cinescout.database.model.id.DatabaseTmdbMovieId
import cinescout.database.model.id.DatabaseTmdbScreenplayId
import cinescout.database.model.id.DatabaseTmdbTvShowId
import cinescout.database.model.id.DatabaseTraktMovieId
import cinescout.database.model.id.DatabaseTraktScreenplayId
import cinescout.database.model.id.DatabaseTraktTvShowId
import cinescout.database.model.id.DatabaseTvShowIds
import cinescout.screenplay.domain.model.TmdbGenreId
import cinescout.screenplay.domain.model.TmdbKeywordId
import cinescout.screenplay.domain.model.ids.MovieIds
import cinescout.screenplay.domain.model.ids.TmdbMovieId
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.screenplay.domain.model.ids.TmdbTvShowId
import cinescout.screenplay.domain.model.ids.TraktMovieId
import cinescout.screenplay.domain.model.ids.TraktScreenplayId
import cinescout.screenplay.domain.model.ids.TraktTvShowId
import cinescout.screenplay.domain.model.ids.TvShowIds

fun TmdbKeywordId.toDatabaseId() = DatabaseTmdbKeywordId(value)
fun TmdbGenreId.toDatabaseId() = DatabaseTmdbGenreId(value)
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

fun TraktScreenplayId.toStringDatabaseId() = value.toString()
fun TraktMovieId.toDatabaseId() = DatabaseTraktMovieId(value)
fun TraktTvShowId.toDatabaseId() = DatabaseTraktTvShowId(value)

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
fun DatabaseTmdbGenreId.toDomainId() = TmdbGenreId(value)
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
fun DatabaseTraktScreenplayId.toTvShowDomainId(): TraktTvShowId = when (this) {
    is DatabaseTraktMovieId -> error("Expected a tv show id, but got a movie id: $this")
    is DatabaseTraktTvShowId -> TraktTvShowId(value)
}
