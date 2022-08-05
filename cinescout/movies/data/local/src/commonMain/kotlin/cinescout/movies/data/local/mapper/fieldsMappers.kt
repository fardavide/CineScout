package cinescout.movies.data.local.mapper

import cinescout.database.model.DatabaseTmdbMovieId
import cinescout.database.model.DatabaseTmdbPersonId
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.model.TmdbPersonId

internal fun DatabaseTmdbMovieId.toId() = TmdbMovieId(value)
internal fun DatabaseTmdbPersonId.toId() = TmdbPersonId(value)
internal fun Rating.toDatabaseRating() = value
internal fun TmdbPersonId.toDatabaseId() = DatabaseTmdbPersonId(value)
internal fun TmdbMovieId.toDatabaseId() = DatabaseTmdbMovieId(value)
