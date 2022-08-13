package cinescout.movies.domain.usecase

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import cinescout.store.PagedStore
import cinescout.store.Paging

class GetAllWatchlistMovies(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(): PagedStore<Movie, Paging.Page.DualSources> =
        movieRepository.getAllWatchlistMovies()
}
