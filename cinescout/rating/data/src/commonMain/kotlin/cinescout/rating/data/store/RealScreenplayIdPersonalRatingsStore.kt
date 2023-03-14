package cinescout.rating.data.store

import cinescout.rating.data.datasource.LocalPersonalRatingDataSource
import cinescout.rating.data.datasource.RemotePersonalRatingDataSource
import cinescout.rating.domain.model.ScreenplayIdWithPersonalRating
import cinescout.rating.domain.store.ScreenplayIdPersonalRatingsStore
import cinescout.store5.EitherFetcher
import cinescout.store5.EitherUpdater
import cinescout.store5.MutableStore5
import cinescout.store5.Store5Builder
import cinescout.store5.empty
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.Bookkeeper
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [ScreenplayIdPersonalRatingsStore::class])
internal class RealScreenplayIdPersonalRatingsStore(
    private val localDataSource: LocalPersonalRatingDataSource,
    private val remoteDataSource: RemotePersonalRatingDataSource
) : ScreenplayIdPersonalRatingsStore,
    MutableStore5<Unit, List<ScreenplayIdWithPersonalRating>, Unit> by Store5Builder
        .from<Unit, List<ScreenplayIdWithPersonalRating>>(
            fetcher = EitherFetcher.of { remoteDataSource.getRatingIds() },
            sourceOfTruth = SourceOfTruth.of(
                reader = { localDataSource.findRatingIds() },
                writer = { _, ratings -> localDataSource.insertRatings(ratings) }
            )
        )
        .build(
            updater = EitherUpdater.by({ _, ratings -> remoteDataSource.postRatings(ratings) }),
            bookkeeper = Bookkeeper.empty()
        )
