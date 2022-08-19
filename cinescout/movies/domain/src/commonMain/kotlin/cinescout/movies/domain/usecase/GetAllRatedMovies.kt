package cinescout.movies.domain.usecase

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.MovieWithPersonalRating
import store.PagedStore
import store.Paging

class GetAllRatedMovies(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(): PagedStore<MovieWithPersonalRating, Paging.Page.DualSources> =
        movieRepository.getAllRatedMovies()
}
