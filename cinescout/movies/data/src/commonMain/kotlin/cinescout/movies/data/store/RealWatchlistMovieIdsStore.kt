package cinescout.movies.data.store

import cinescout.movies.data.LocalMovieDataSource
import cinescout.movies.data.RemoteMovieDataSource
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.store.WatchlistMovieIdsStore
import cinescout.screenplay.domain.model.ids
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

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
