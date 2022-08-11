package cinescout.movies.data.local.mapper

import cinescout.database.model.DatabaseTmdbGenreId
import cinescout.database.model.DatabaseTmdbKeywordId
import cinescout.database.model.DatabaseTmdbMovieId
import cinescout.database.model.DatabaseTmdbPersonId
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbGenreId
import cinescout.movies.domain.model.TmdbKeywordId
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.model.TmdbPersonId

internal fun DatabaseTmdbGenreId.toId() = TmdbGenreId(value)
internal fun DatabaseTmdbKeywordId.toId() = TmdbKeywordId(value)
internal fun DatabaseTmdbMovieId.toId() = TmdbMovieId(value)
internal fun DatabaseTmdbPersonId.toId() = TmdbPersonId(value)
internal fun Rating.toDatabaseRating() = value
internal fun TmdbGenreId.toDatabaseId() = DatabaseTmdbGenreId(value)
internal fun TmdbKeywordId.toDatabaseId() = DatabaseTmdbKeywordId(value)
internal fun TmdbPersonId.toDatabaseId() = DatabaseTmdbPersonId(value)
internal fun TmdbMovieId.toDatabaseId() = DatabaseTmdbMovieId(value)
