package cinescout.tvshows.data.store

import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import cinescout.tvshows.data.LocalTvShowDataSource
import cinescout.tvshows.data.RemoteTvShowDataSource
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowWithDetails
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface TvShowDetailsStore : Store5<TvShowDetailsKey, TvShowWithDetails>

@Single(binds = [TvShowDetailsStore::class])
internal class RealTvShowDetailsStore(
    private val localTvShowDataSource: LocalTvShowDataSource,
    private val remoteTvShowDataSource: RemoteTvShowDataSource
) : TvShowDetailsStore,
    Store5<TvShowDetailsKey, TvShowWithDetails> by Store5Builder
        .from<TvShowDetailsKey, TvShowWithDetails>(
            fetcher = EitherFetcher.of { key -> remoteTvShowDataSource.getTvShowDetails(key.tvShowId) },
            sourceOfTruth = SourceOfTruth.Companion.of(
                reader = { key -> localTvShowDataSource.findTvShowWithDetails(key.tvShowId) },
                writer = { _, value -> localTvShowDataSource.insert(value) }
            )
        )
        .build()

@JvmInline
value class TvShowDetailsKey(val tvShowId: TmdbTvShowId)

class FakeTvShowDetailsStore(private val tvShowsDetails: List<TvShowWithDetails>) :
    TvShowDetailsStore {

    override fun stream(request: StoreReadRequest<TvShowDetailsKey>): StoreFlow<TvShowWithDetails> =
        storeFlowOf(tvShowsDetails.first { it.tvShow.tmdbId == request.key.tvShowId })
}
