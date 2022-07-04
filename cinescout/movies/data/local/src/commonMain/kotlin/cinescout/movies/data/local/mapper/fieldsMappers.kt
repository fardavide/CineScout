package mapper

import cinescout.database.model.DatabaseTmdbMovieId
import cinescout.movies.domain.model.TmdbMovieId

internal fun TmdbMovieId.toDatabaseId() = DatabaseTmdbMovieId(value)
internal fun DatabaseTmdbMovieId.toId() = TmdbMovieId(value)
