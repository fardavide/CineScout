package cinescout.movies.domain.usecase

import cinescout.error.DataError
import cinescout.movies.domain.MovieRepository
import cinescout.movies.domain.model.Movie
import org.koin.core.annotation.Factory
import store.PagedStore
import store.Paging
import store.Refresh
import store.builder.pagedStoreOf
import kotlin.time.Duration.Companion.days

interface GetAllWatchlistMovies {

    operator fun invoke(refresh: Refresh = Refresh.IfExpired(1.days)): PagedStore<Movie, Paging>
}

@Factory
class RealGetAllWatchlistMovies(
    private val movieRepository: MovieRepository
) : GetAllWatchlistMovies {

    override operator fun invoke(refresh: Refresh): PagedStore<Movie, Paging> =
        movieRepository.getAllWatchlistMovies(refresh)
}

class FakeGetAllWatchlistMovies(
    private val watchlist: List<Movie>? = null,
    private val pagedStore: PagedStore<Movie, Paging> =
        watchlist?.let(::pagedStoreOf) ?: pagedStoreOf(DataError.Local.NoCache)
) : GetAllWatchlistMovies {

    override operator fun invoke(refresh: Refresh): PagedStore<Movie, Paging> = pagedStore
}
