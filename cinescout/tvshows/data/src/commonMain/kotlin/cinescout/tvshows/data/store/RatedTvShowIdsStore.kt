package cinescout.tvshows.data.store

import cinescout.error.NetworkError
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import cinescout.tvshows.data.LocalTvShowDataSource
import cinescout.tvshows.data.RemoteTvShowDataSource
import cinescout.tvshows.domain.model.TvShowIdWithPersonalRating
import cinescout.tvshows.domain.model.TvShowWithPersonalRating
import cinescout.tvshows.domain.model.ids
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface RatedTvShowIdsStore : Store5<Unit, List<TvShowIdWithPersonalRating>>

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

class FakeRatedTvShowIdsStore(
    private val tvShows: List<TvShowWithPersonalRating>? = null,
    private val tvShowIds: List<TvShowIdWithPersonalRating>? = tvShows?.ids()
) : RatedTvShowIdsStore {

    override fun stream(request: StoreReadRequest<Unit>): StoreFlow<List<TvShowIdWithPersonalRating>> =
        tvShowIds?.let(::storeFlowOf) ?: storeFlowOf(NetworkError.NotFound)
}
