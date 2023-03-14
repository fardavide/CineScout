package cinescout.movies.data.local.mapper

import cinescout.database.model.DatabaseTmdbGenreId
import cinescout.database.model.DatabaseTmdbKeywordId
import cinescout.database.model.DatabaseTmdbMovieId
import cinescout.database.model.DatabaseTmdbTvShowId
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.TmdbGenreId
import cinescout.screenplay.domain.model.TmdbKeywordId
import cinescout.screenplay.domain.model.TmdbScreenplayId

internal fun DatabaseTmdbGenreId.toId() = TmdbGenreId(value)
internal fun DatabaseTmdbKeywordId.toId() = TmdbKeywordId(value)
internal fun DatabaseTmdbMovieId.toId() = TmdbMovieId(value)
internal fun Rating.toDatabaseRating() = value
internal fun TmdbGenreId.toDatabaseId() = DatabaseTmdbGenreId(value)
internal fun TmdbKeywordId.toDatabaseId() = DatabaseTmdbKeywordId(value)
fun TmdbMovieId.toDatabaseId() = DatabaseTmdbMovieId(value)
fun TmdbMovieId.toScreenplayDatabaseId() = DatabaseTmdbMovieId(value)
fun TmdbScreenplayId.toScreenplayDatabaseId() = when (this) {
    is TmdbScreenplayId.Movie -> DatabaseTmdbMovieId(value)
    is TmdbScreenplayId.TvShow -> DatabaseTmdbTvShowId(value)
}
