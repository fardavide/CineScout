package cinescout.screenplay.data.local.mapper

import cinescout.database.model.DatabaseTmdbGenreId
import cinescout.database.model.DatabaseTmdbKeywordId
import cinescout.database.model.DatabaseTmdbMovieId
import cinescout.database.model.DatabaseTmdbScreenplayId
import cinescout.database.model.DatabaseTmdbTvShowId
import cinescout.database.model.DatabaseTraktMovieId
import cinescout.database.model.DatabaseTraktScreenplayId
import cinescout.database.model.DatabaseTraktTvShowId
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
fun TmdbScreenplayId.toScreenplayDatabaseId() = when (this) {
    is TmdbScreenplayId.Movie -> DatabaseTmdbMovieId(value)
    is TmdbScreenplayId.TvShow -> DatabaseTmdbTvShowId(value)
}
fun TmdbScreenplayId.toStringDatabaseId() = value.toString()
fun TmdbScreenplayId.Movie.toDatabaseId() = DatabaseTmdbMovieId(value)
fun TmdbScreenplayId.TvShow.toDatabaseId() = DatabaseTmdbTvShowId(value)
fun TraktScreenplayId.toScreenplayDatabaseId() = when (this) {
    is TraktScreenplayId.Movie -> DatabaseTmdbMovieId(value)
    is TraktScreenplayId.TvShow -> DatabaseTmdbTvShowId(value)
}
fun TraktScreenplayId.toStringDatabaseId() = value.toString()
fun TraktScreenplayId.Movie.toDatabaseId() = DatabaseTraktMovieId(value)
fun TraktScreenplayId.TvShow.toDatabaseId() = DatabaseTraktTvShowId(value)

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
