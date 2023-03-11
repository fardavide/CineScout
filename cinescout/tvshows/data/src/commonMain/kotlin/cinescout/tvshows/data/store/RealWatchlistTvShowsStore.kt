package cinescout.tvshows.data.store

import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import cinescout.model.NetworkOperation
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import cinescout.tvshows.data.LocalTvShowDataSource
import cinescout.tvshows.data.RemoteTvShowDataSource
import cinescout.tvshows.domain.model.TvShow
import cinescout.tvshows.domain.store.TvShowDetailsKey
import cinescout.tvshows.domain.store.TvShowDetailsStore
import cinescout.tvshows.domain.store.WatchlistTvShowsStore
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [WatchlistTvShowsStore::class])
internal class RealWatchlistTvShowsStore(
    private val localTvShowDataSource: LocalTvShowDataSource,
    private val tvShowDetailsStore: TvShowDetailsStore,
    private val remoteTvShowDataSource: RemoteTvShowDataSource
) : WatchlistTvShowsStore,
    Store5<Unit, List<TvShow>> by Store5Builder
        .from<Unit, List<TvShow>>(
            fetcher = EitherFetcher.buildForOperation {
                either {
                    val ratedIds = remoteTvShowDataSource.getWatchlistTvShows()
                        .bind()

                    if (ratedIds.isEmpty()) {
                        emit(emptyList<TvShow>().right())
                        return@either
                    }

                    ratedIds.fold(emptyList<TvShow>()) { acc, tvShowId ->
                        val details = tvShowDetailsStore.get(TvShowDetailsKey(tvShowId))
                            .mapLeft(NetworkOperation::Error)
                            .bind()

                        (acc + details.tvShow).also { list -> emit(list.right()) }
                    }
                }.onLeft { emit(it.left()) }
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = { localTvShowDataSource.findAllWatchlistTvShows() },
                writer = { _, value -> localTvShowDataSource.insertWatchlist(value) }
            )
        )
        .build()
