package cinescout.screenplay.data.local.mapper

import cinescout.database.model.DatabaseTmdbMovieId
import cinescout.database.model.DatabaseTmdbScreenplayId
import cinescout.database.model.DatabaseTmdbTvShowId
import cinescout.screenplay.domain.model.TmdbScreenplayId

fun TmdbScreenplayId.toDatabaseId(): DatabaseTmdbScreenplayId = when (this) {
    is TmdbScreenplayId.Movie -> DatabaseTmdbMovieId(value)
    is TmdbScreenplayId.TvShow -> DatabaseTmdbTvShowId(value)
}
fun TmdbScreenplayId.Movie.toDatabaseId(): DatabaseTmdbMovieId = DatabaseTmdbMovieId(value)
fun TmdbScreenplayId.TvShow.toDatabaseId(): DatabaseTmdbTvShowId = DatabaseTmdbTvShowId(value)
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
