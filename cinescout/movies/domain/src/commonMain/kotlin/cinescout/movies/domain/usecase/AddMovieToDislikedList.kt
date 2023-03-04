package cinescout.movies.domain.usecase

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.TmdbMovieId
import org.koin.core.annotation.Factory

interface AddMovieToDislikedList {

    suspend operator fun invoke(movieId: TmdbMovieId)
}

@Factory
class RealAddMovieToDislikedList(
    private val movieRepository: MovieRepository
) : AddMovieToDislikedList {

    override suspend operator fun invoke(movieId: TmdbMovieId) {
        movieRepository.addToDisliked(movieId)
    }
}

class FakeAddMovieToDislikedList : AddMovieToDislikedList {

    override suspend operator fun invoke(movieId: TmdbMovieId) {
        // no-op
    }
}
