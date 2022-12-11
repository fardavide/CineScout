package cinescout.movies.domain.usecase

import arrow.core.Either
import cinescout.common.model.Rating
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.TmdbMovieId
import org.koin.core.annotation.Factory

@Factory
class RateMovie(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(movieId: TmdbMovieId, rating: Rating): Either<DataError, Unit> =
        movieRepository.rate(movieId, rating)
}
