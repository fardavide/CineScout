package cinescout.tvshows.data.store

import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import cinescout.tvshows.data.LocalTvShowDataSource
import cinescout.tvshows.data.RemoteTvShowDataSource
import cinescout.tvshows.domain.model.TvShowIdWithPersonalRating
import cinescout.tvshows.domain.model.ids
import cinescout.tvshows.domain.store.RatedTvShowIdsStore
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [RatedTvShowIdsStore::class])
class RealRatedTvShowIdsStore(
    private val localTvShowDataSource: LocalTvShowDataSource,
    private val remoteTvShowDataSource: RemoteTvShowDataSource
) : RatedTvShowIdsStore,
    Store5<Unit, List<TvShowIdWithPersonalRating>> by Store5Builder
        .from<Unit, List<TvShowIdWithPersonalRating>>(
            fetcher = EitherFetcher.ofOperation { remoteTvShowDataSource.getRatedTvShows() },
            sourceOfTruth = SourceOfTruth.of(
                reader = { localTvShowDataSource.findAllRatedTvShows().ids() },
                writer = { _, value -> localTvShowDataSource.insertRatingIds(value) }
            )
        )
        .build()
