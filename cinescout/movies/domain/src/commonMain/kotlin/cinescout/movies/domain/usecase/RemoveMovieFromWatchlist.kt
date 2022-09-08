package cinescout.movies.domain.usecase

import arrow.core.Either
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.TmdbMovieId

class RemoveMovieFromWatchlist(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(movieId: TmdbMovieId): Either<DataError.Remote, Unit> =
        movieRepository.removeFromWatchlist(movieId)
}
