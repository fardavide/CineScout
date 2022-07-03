package cinescout.movies.domain.usecase

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie

class AddMovieToWatchlist(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(movie: Movie) {
        movieRepository.addToWatchlist(movie)
    }
}
