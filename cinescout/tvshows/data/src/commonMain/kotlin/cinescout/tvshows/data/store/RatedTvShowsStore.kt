package cinescout.tvshows.data.store

import arrow.core.continuations.either
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.store5.EitherFetcher
import cinescout.store5.Store5
import cinescout.store5.Store5Builder
import cinescout.store5.StoreFlow
import cinescout.store5.test.storeFlowOf
import cinescout.tvshows.data.LocalTvShowDataSource
import cinescout.tvshows.data.RemoteTvShowDataSource
import cinescout.tvshows.domain.model.TvShowWithPersonalRating
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreReadRequest

interface RatedTvShowsStore : Store5<Unit, List<TvShowWithPersonalRating>>

@Single(binds = [RatedTvShowsStore::class])
class RealRatedTvShowsStore(
    private val localTvShowDataSource: LocalTvShowDataSource,
    private val tvShowDetailsStore: TvShowDetailsStore,
    private val remoteTvShowDataSource: RemoteTvShowDataSource
) : RatedTvShowsStore,
    Store5<Unit, List<TvShowWithPersonalRating>> by Store5Builder
        .from<Unit, List<TvShowWithPersonalRating>>(
            fetcher = EitherFetcher.buildForOperation {
                either {
                    val ratedIds = remoteTvShowDataSource.getRatedTvShows()
                        .bind()

                    if (ratedIds.isEmpty()) {
                        emit(emptyList<TvShowWithPersonalRating>().right())
                        return@either
                    }

                    ratedIds.fold(emptyList<TvShowWithPersonalRating>()) { acc, (tvShowId, personalRating) ->
                        val details = tvShowDetailsStore.get(TvShowDetailsKey(tvShowId))
                            .mapLeft(NetworkOperation::Error)
                            .bind()
                        val tvShowWithPersonalRating = TvShowWithPersonalRating(details.tvShow, personalRating)

                        (acc + tvShowWithPersonalRating).also { list -> emit(list.right()) }
                    }
                }.onLeft { emit(it.left()) }
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = { localTvShowDataSource.findAllRatedTvShows() },
                writer = { _, value -> localTvShowDataSource.insertRatings(value) }
            )
        )
        .build()

class FakeRatedTvShowsStore(
    private val tvShows: List<TvShowWithPersonalRating>? = null
) : RatedTvShowsStore {

    override fun stream(request: StoreReadRequest<Unit>): StoreFlow<List<TvShowWithPersonalRating>> =
        tvShows?.let(::storeFlowOf) ?: storeFlowOf(NetworkError.NotFound)
}
