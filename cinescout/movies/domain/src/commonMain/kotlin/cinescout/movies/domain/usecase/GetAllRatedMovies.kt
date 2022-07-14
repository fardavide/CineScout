package cinescout.movies.domain.usecase

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.MovieWithRating
import cinescout.utils.kotlin.PagedStore

class GetAllRatedMovies(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(): PagedStore<MovieWithRating> =
        movieRepository.getAllRatedMovies()
}
