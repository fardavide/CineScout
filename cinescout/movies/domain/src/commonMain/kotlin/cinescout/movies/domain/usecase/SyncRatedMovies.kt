package cinescout.movies.domain.usecase

import cinescout.movies.domain.MovieRepository

class SyncRatedMovies(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke() {
        movieRepository.syncRatedMovies()
    }
}
