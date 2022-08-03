package cinescout.movies.domain.usecase

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.MovieWithRating
import cinescout.store.PagedStore
import cinescout.store.Paging

class GetAllRatedMovies(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(): PagedStore<MovieWithRating, Paging.Page.DualSources> =
        movieRepository.getAllRatedMovies()
}
