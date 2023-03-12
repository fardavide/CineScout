package cinescout.movies.domain.store

import cinescout.error.NetworkError
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.screenplay.domain.model.Movie
import cinescout.store5.Store5
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface WatchlistMovieIdsStore : Store5<Unit, List<TmdbMovieId>>

class FakeWatchlistMovieIdsStore(
    private val movies: List<Movie>? = null,
    private val movieIds: List<TmdbMovieId>? =
        movies?.map { it.tmdbId }
) : WatchlistMovieIdsStore {

    override suspend fun clear() {
        TODO("Not yet implemented")
    }

    override fun stream(request: StoreReadRequest<Unit>): StoreFlow<List<TmdbMovieId>> =
        movieIds?.let(::storeFlowOf) ?: storeFlowOf(NetworkError.NotFound)
}
