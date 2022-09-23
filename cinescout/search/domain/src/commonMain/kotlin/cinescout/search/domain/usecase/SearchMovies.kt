package cinescout.search.domain.usecase

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import store.PagedStore
import store.Paging

class SearchMovies(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(query: String): PagedStore<Movie, Paging> =
        movieRepository.searchMovies(query)
}
