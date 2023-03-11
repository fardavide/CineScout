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
import cinescout.tvshows.domain.model.TvShowWithPersonalRating
import cinescout.tvshows.domain.store.RatedTvShowsStore
import cinescout.tvshows.domain.store.TvShowDetailsKey
import cinescout.tvshows.domain.store.TvShowDetailsStore
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

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
