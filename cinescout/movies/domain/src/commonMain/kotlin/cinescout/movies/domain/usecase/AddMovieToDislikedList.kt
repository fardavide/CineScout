package cinescout.movies.domain.usecase

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.TmdbMovieId

class AddMovieToDislikedList(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(movieId: TmdbMovieId) {
        movieRepository.addToDisliked(movieId)
    }
}
