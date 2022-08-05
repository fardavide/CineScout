package cinescout.movies.domain.usecase

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.MovieWithPersonalRating
import cinescout.store.PagedStore
import cinescout.store.Paging

class GetAllRatedMovies(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(): PagedStore<MovieWithPersonalRating, Paging.Page.DualSources> =
        movieRepository.getAllRatedMovies()
}
