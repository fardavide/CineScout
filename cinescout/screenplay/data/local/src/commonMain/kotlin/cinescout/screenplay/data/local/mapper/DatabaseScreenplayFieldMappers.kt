package cinescout.screenplay.data.local.mapper

import cinescout.database.model.DatabaseMovieIds
import cinescout.database.model.DatabaseScreenplayIds
import cinescout.database.model.DatabaseTmdbGenreId
import cinescout.database.model.DatabaseTmdbKeywordId
import cinescout.database.model.DatabaseTmdbMovieId
import cinescout.database.model.DatabaseTmdbScreenplayId
import cinescout.database.model.DatabaseTmdbTvShowId
import cinescout.database.model.DatabaseTraktMovieId
import cinescout.database.model.DatabaseTraktScreenplayId
import cinescout.database.model.DatabaseTraktTvShowId
import cinescout.database.model.DatabaseTvShowIds
import cinescout.screenplay.domain.model.ScreenplayIds
import cinescout.screenplay.domain.model.TmdbGenreId
import cinescout.screenplay.domain.model.TmdbKeywordId
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.model.TraktScreenplayId

fun TmdbKeywordId.toDatabaseId() = DatabaseTmdbKeywordId(value)
fun TmdbGenreId.toDatabaseId() = DatabaseTmdbGenreId(value)
fun TmdbScreenplayId.toDatabaseId(): DatabaseTmdbScreenplayId = when (this) {
    is TmdbScreenplayId.Movie -> DatabaseTmdbMovieId(value)
    is TmdbScreenplayId.TvShow -> DatabaseTmdbTvShowId(value)
}

fun TmdbScreenplayId.toStringDatabaseId() = value.toString()
fun TmdbScreenplayId.Movie.toDatabaseId() = DatabaseTmdbMovieId(value)
fun TmdbScreenplayId.TvShow.toDatabaseId() = DatabaseTmdbTvShowId(value)
fun TraktScreenplayId.toDatabaseId(): DatabaseTraktScreenplayId = when (this) {
    is TraktScreenplayId.Movie -> DatabaseTraktMovieId(value)
    is TraktScreenplayId.TvShow -> DatabaseTraktTvShowId(value)
}

fun TraktScreenplayId.toStringDatabaseId() = value.toString()
fun TraktScreenplayId.Movie.toDatabaseId() = DatabaseTraktMovieId(value)
fun TraktScreenplayId.TvShow.toDatabaseId() = DatabaseTraktTvShowId(value)

fun DatabaseScreenplayIds.toDomainIds() = when (this) {
    is DatabaseMovieIds -> ScreenplayIds.Movie(
        tmdb = TmdbScreenplayId.Movie(tmdb.value),
        trakt = TraktScreenplayId.Movie(trakt.value)
    )
    is DatabaseTvShowIds -> ScreenplayIds.TvShow(
        tmdb = TmdbScreenplayId.TvShow(tmdb.value),
        trakt = TraktScreenplayId.TvShow(trakt.value)
    )
}
fun DatabaseScreenplayIds.toMovieDomainIds() = ScreenplayIds.Movie(
    tmdb = TmdbScreenplayId.Movie(tmdb.value),
    trakt = TraktScreenplayId.Movie(trakt.value)
)
fun DatabaseScreenplayIds.toTvShowDomainIds() = ScreenplayIds.TvShow(
    tmdb = TmdbScreenplayId.TvShow(tmdb.value),
    trakt = TraktScreenplayId.TvShow(trakt.value)
)
fun DatabaseTmdbKeywordId.toDomainId() = TmdbKeywordId(value)
fun DatabaseTmdbGenreId.toDomainId() = TmdbGenreId(value)
fun DatabaseTmdbScreenplayId.toDomainId(): TmdbScreenplayId = when (this) {
    is DatabaseTmdbMovieId -> TmdbScreenplayId.Movie(value)
    is DatabaseTmdbTvShowId -> TmdbScreenplayId.TvShow(value)
}
fun DatabaseTmdbScreenplayId.toMovieDomainId(): TmdbScreenplayId.Movie = when (this) {
    is DatabaseTmdbMovieId -> TmdbScreenplayId.Movie(value)
    is DatabaseTmdbTvShowId -> error("Expected a movie id, but got a tv show id: $this")
}
fun DatabaseTmdbScreenplayId.toTvShowDomainId(): TmdbScreenplayId.TvShow = when (this) {
    is DatabaseTmdbMovieId -> error("Expected a tv show id, but got a movie id: $this")
    is DatabaseTmdbTvShowId -> TmdbScreenplayId.TvShow(value)
}
fun DatabaseTraktScreenplayId.toMovieDomainId(): TraktScreenplayId.Movie = when (this) {
    is DatabaseTraktMovieId -> TraktScreenplayId.Movie(value)
    is DatabaseTraktTvShowId -> error("Expected a movie id, but got a tv show id: $this")
}
fun DatabaseTraktScreenplayId.toTvShowDomainId(): TraktScreenplayId.TvShow = when (this) {
    is DatabaseTraktMovieId -> error("Expected a tv show id, but got a movie id: $this")
    is DatabaseTraktTvShowId -> TraktScreenplayId.TvShow(value)
}
