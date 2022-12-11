package cinescout.movies.domain.usecase

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.TmdbMovieId
import org.koin.core.annotation.Factory

@Factory
class AddMovieToDislikedList(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(movieId: TmdbMovieId) {
        movieRepository.addToDisliked(movieId)
    }
}
