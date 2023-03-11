package cinescout.movies.domain.usecase

import cinescout.error.NetworkError
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.store.WatchlistMoviesStore
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import org.koin.core.annotation.Factory
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface GetAllWatchlistMovies {

    operator fun invoke(refresh: Boolean = true): StoreFlow<List<Movie>>
}

@Factory
class RealGetAllWatchlistMovies(
    private val watchlistMoviesStore: WatchlistMoviesStore
) : GetAllWatchlistMovies {

    override operator fun invoke(refresh: Boolean): StoreFlow<List<Movie>> =
        watchlistMoviesStore.stream(StoreReadRequest.cached(Unit, refresh = refresh))
}

class FakeGetAllWatchlistMovies(
    private val watchlist: List<Movie>? = null,
    private val store: StoreFlow<List<Movie>> =
        watchlist?.let(::storeFlowOf) ?: storeFlowOf(NetworkError.NotFound)
) : GetAllWatchlistMovies {

    override operator fun invoke(refresh: Boolean): StoreFlow<List<Movie>> = store
}
