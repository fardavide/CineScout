package cinescout.movies.domain.usecase

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.TmdbMovieId
import org.koin.core.annotation.Factory

interface AddMovieToLikedList {

    suspend operator fun invoke(movieId: TmdbMovieId)
}

@Factory
class RealAddMovieToLikedList(
    private val movieRepository: MovieRepository
) : AddMovieToLikedList {

    override suspend operator fun invoke(movieId: TmdbMovieId) {
        movieRepository.addToLiked(movieId)
    }
}

class FakeAddMovieToLikedList : AddMovieToLikedList {

    override suspend operator fun invoke(movieId: TmdbMovieId) {
        // no-op
    }
}
