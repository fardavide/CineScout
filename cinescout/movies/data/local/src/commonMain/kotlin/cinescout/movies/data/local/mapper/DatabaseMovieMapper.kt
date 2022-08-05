package cinescout.movies.data.local.mapper

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.left
import arrow.core.valueOr
import cinescout.database.FindAllWithRating
import cinescout.database.model.DatabaseMovie
import cinescout.error.DataError
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.movies.domain.model.Rating

internal class DatabaseMovieMapper {

    fun toMovie(databaseMovie: DatabaseMovie) = Movie(
        title = databaseMovie.title,
        releaseDate = databaseMovie.releaseDate,
        tmdbId = databaseMovie.tmdbId.toId()
    )

    suspend fun toMoviesWithRating(
        list: List<FindAllWithRating>
    ): Either<DataError.Local, List<MovieWithPersonalRating>> {
        if (list.isEmpty()) {
            return DataError.Local.NoCache.left()
        }
        return either {
            list.map { entry ->
                val rating = Rating.of(entry.rating)
                    .valueOr { throw IllegalStateException("Invalid rating: $it") }

                MovieWithPersonalRating(
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
