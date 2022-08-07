package cinescout.movies.domain.usecase

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.TmdbMovieId

class AddMovieToLikedList(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(movieId: TmdbMovieId) {
        movieRepository.addToLiked(movieId)
    }
}
