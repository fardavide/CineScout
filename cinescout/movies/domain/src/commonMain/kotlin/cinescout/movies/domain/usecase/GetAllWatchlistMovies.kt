package cinescout.movies.domain.usecase

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import store.PagedStore
import store.Paging

class GetAllWatchlistMovies(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(): PagedStore<Movie, Paging.Page.DualSources> =
        movieRepository.getAllWatchlistMovies()
}
