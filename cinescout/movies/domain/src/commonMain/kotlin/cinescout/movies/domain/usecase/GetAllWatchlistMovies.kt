package cinescout.movies.domain.usecase

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import store.PagedStore
import store.Paging
import store.Refresh
import kotlin.time.Duration.Companion.days

class GetAllWatchlistMovies(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(refresh: Refresh = Refresh.IfExpired(1.days)): PagedStore<Movie, Paging> =
        movieRepository.getAllWatchlistMovies(refresh)
}
