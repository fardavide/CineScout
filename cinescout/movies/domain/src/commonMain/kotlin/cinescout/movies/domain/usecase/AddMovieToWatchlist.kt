package cinescout.movies.domain.usecase

import arrow.core.Either
import arrow.core.right
import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.TmdbMovieId
import org.koin.core.annotation.Factory

interface AddMovieToWatchlist {

    suspend operator fun invoke(movieId: TmdbMovieId): Either<DataError.Remote, Unit>
}

@Factory
class RealAddMovieToWatchlist(
    private val movieRepository: MovieRepository
) : AddMovieToWatchlist {

    override suspend operator fun invoke(movieId: TmdbMovieId): Either<DataError.Remote, Unit> =
        movieRepository.addToWatchlist(movieId)
}

class FakeAddMovieToWatchlist : AddMovieToWatchlist {

    override suspend operator fun invoke(movieId: TmdbMovieId): Either<DataError.Remote, Unit> = Unit.right()
}
