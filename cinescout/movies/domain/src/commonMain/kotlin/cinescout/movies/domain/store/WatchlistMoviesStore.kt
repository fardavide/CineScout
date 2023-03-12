package cinescout.movies.domain.store

import cinescout.error.NetworkError
import cinescout.screenplay.domain.model.Movie
import cinescout.store5.Store5
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface WatchlistMoviesStore : Store5<Unit, List<Movie>>

class FakeWatchlistMoviesStore(
    private val movies: List<Movie>? = null
) : WatchlistMoviesStore {

    override suspend fun clear() {
        TODO("Not yet implemented")
    }

    override fun stream(request: StoreReadRequest<Unit>): StoreFlow<List<Movie>> =
        movies?.let(::storeFlowOf) ?: storeFlowOf(NetworkError.NotFound)
}
