package cinescout.screenplay.data.local.mapper

import cinescout.database.model.DatabaseTmdbGenreId
import cinescout.database.model.DatabaseTmdbKeywordId
import cinescout.database.model.DatabaseTmdbMovieId
import cinescout.database.model.DatabaseTmdbScreenplayId
import cinescout.database.model.DatabaseTmdbTvShowId
import cinescout.screenplay.domain.model.TmdbGenreId
import cinescout.screenplay.domain.model.TmdbKeywordId
import cinescout.screenplay.domain.model.TmdbScreenplayId

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
