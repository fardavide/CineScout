package cinescout.movies.data.store

import cinescout.error.NetworkError
import cinescout.movies.data.LocalMovieDataSource
import cinescout.movies.data.RemoteMovieDataSource
import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.model.ids
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface WatchlistMovieIdsStore : Store5<Unit, List<TmdbMovieId>>

@Single(binds = [WatchlistMovieIdsStore::class])
class RealWatchlistMovieIdsStore(
    private val localMovieDataSource: LocalMovieDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource
) : WatchlistMovieIdsStore,
    Store5<Unit, List<TmdbMovieId>> by Store5Builder
        .from<Unit, List<TmdbMovieId>>(
            fetcher = EitherFetcher.ofOperation { remoteMovieDataSource.getWatchlistMovies() },
            sourceOfTruth = SourceOfTruth.of(
                reader = { localMovieDataSource.findAllWatchlistMovies().ids() },
                writer = { _, value -> localMovieDataSource.insertWatchlistIds(value) }
            )
        )
        .build()

class FakeWatchlistMovieIdsStore(
    private val movies: List<Movie>? = null,
    private val movieIds: List<TmdbMovieId>? =
        movies?.map { it.tmdbId }
) : WatchlistMovieIdsStore {

    override fun stream(request: StoreReadRequest<Unit>): StoreFlow<List<TmdbMovieId>> =
        movieIds?.let(::storeFlowOf) ?: storeFlowOf(NetworkError.NotFound)
}
