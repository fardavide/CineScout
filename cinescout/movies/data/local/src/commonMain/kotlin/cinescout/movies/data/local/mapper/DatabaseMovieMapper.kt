package cinescout.movies.data.local.mapper

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cinescout.database.model.DatabaseMovie
import cinescout.error.DataError
import cinescout.movies.domain.model.Movie

internal class DatabaseMovieMapper {

    fun toMovie(databaseMovie: DatabaseMovie?): Either<DataError.Local, Movie> =
        if (databaseMovie != null) {
            Movie(
                title = databaseMovie.title,
                tmdbId = databaseMovie.tmdbId.toId()
            ).right()
        } else {
            DataError.Local.NoCache.left()
        }
}
