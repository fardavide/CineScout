package cinescout.movies.domain.store

import cinescout.error.NetworkError
import cinescout.movies.domain.model.Movie
import cinescout.store5.Store5
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface WatchlistMoviesStore : Store5<Unit, List<Movie>>

class FakeWatchlistMoviesStore(
    private val movies: List<Movie>? = null
) : WatchlistMoviesStore {

    override fun stream(request: StoreReadRequest<Unit>): StoreFlow<List<Movie>> =
        movies?.let(::storeFlowOf) ?: storeFlowOf(NetworkError.NotFound)
}
