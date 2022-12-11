package cinescout.search.domain.usecase

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import org.koin.core.annotation.Factory
import store.PagedStore
import store.Paging

@Factory
class SearchMovies(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(query: String): PagedStore<Movie, Paging> =
        movieRepository.searchMovies(query)
}
