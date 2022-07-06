package cinescout.movies.data.local.mapper

import cinescout.database.model.DatabaseTmdbMovieId
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId

internal fun DatabaseTmdbMovieId.toId() = TmdbMovieId(value)
internal fun Rating.toDatabaseRating() = value
internal fun TmdbMovieId.toDatabaseId() = DatabaseTmdbMovieId(value)
