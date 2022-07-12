package cinescout.movies.data.local.mapper

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import arrow.core.valueOr
import cinescout.database.FindAllWithRating
import cinescout.database.model.DatabaseMovie
import cinescout.error.DataError
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithRating
import cinescout.movies.domain.model.Rating

internal class DatabaseMovieMapper {

    fun toMovie(databaseMovie: DatabaseMovie?): Either<DataError.Local, Movie> =
        if (databaseMovie != null) {
            Movie(
                title = databaseMovie.title,
                releaseDate = databaseMovie.releaseDate,
                tmdbId = databaseMovie.tmdbId.toId()
            ).right()
        } else {
            DataError.Local.NoCache.left()
        }

    suspend fun toMoviesWithRating(list: List<FindAllWithRating>): Either<DataError.Local, List<MovieWithRating>> {
        if (list.isEmpty()) {
            return DataError.Local.NoCache.left()
        }
        return either {
            list.map { entry ->
                val rating = Rating.of(entry.rating)
                    .valueOr { throw IllegalStateException("Invalid rating: $it") }

                MovieWithRating(
                    movie = Movie(
                        title = entry.title,
                        releaseDate = entry.releaseDate,
                        tmdbId = entry.tmdbId.toId()
                    ),
                    rating = rating
                )
            }
        }
    }
}
