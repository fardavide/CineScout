package cinescout.watchlist.data

import cinescout.lists.domain.ListType
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import cinescout.utils.kotlin.sum
import cinescout.watchlist.domain.WatchlistIdsStore
import cinescout.watchlist.domain.WatchlistIdsStoreKey
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [WatchlistIdsStore::class])
internal class RealWatchlistIdsStore(
    private val localDataSource: LocalWatchlistDataSource,
    private val remoteDataSource: RemoteWatchlistDataSource
) : WatchlistIdsStore,
    Store5<WatchlistIdsStoreKey, List<TmdbScreenplayId>> by Store5Builder
        .from<WatchlistIdsStoreKey, List<TmdbScreenplayId>>(
            fetcher = EitherFetcher.ofOperation { key ->
                when (key.listType) {
                    ListType.All -> sum(
                        remoteDataSource.getAllWatchlistMovies(),
                        remoteDataSource.getAllWatchlistTvShows()
                    )
                    ListType.Movies -> remoteDataSource.getAllWatchlistMovies()
                    ListType.TvShows -> remoteDataSource.getAllWatchlistTvShows()
                }
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = { key -> localDataSource.findWatchlistIds(key.listType) },
                writer = { _, ids -> localDataSource.insertAllWatchlistIds(ids) },
                delete = { _ -> localDataSource.deleteAllWatchlistIds() },
                deleteAll = { localDataSource.deleteAllWatchlistIds() }
            )
        )
        .build()
