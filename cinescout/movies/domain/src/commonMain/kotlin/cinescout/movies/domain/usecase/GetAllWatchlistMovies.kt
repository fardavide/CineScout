package cinescout.movies.domain.usecase

import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import org.koin.core.annotation.Factory
import store.PagedStore
import store.Paging
import store.Refresh
import kotlin.time.Duration.Companion.days

@Factory
class GetAllWatchlistMovies(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(refresh: Refresh = Refresh.IfExpired(1.days)): PagedStore<Movie, Paging> =
        movieRepository.getAllWatchlistMovies(refresh)
}
