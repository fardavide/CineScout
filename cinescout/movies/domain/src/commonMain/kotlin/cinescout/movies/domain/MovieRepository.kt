package cinescout.movies.domain

import arrow.core.Either
import cinescout.error.DataError
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.model.TmdbMovieId

interface MovieRepository {

    suspend fun getMovie(id: TmdbMovieId): Either<DataError, Movie>

    suspend fun addToWatchlist(movie: Movie)

    suspend fun rate(movie: Movie, rating: Rating)
}
