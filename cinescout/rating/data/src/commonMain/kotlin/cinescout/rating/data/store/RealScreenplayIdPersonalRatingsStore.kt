package cinescout.rating.data.store

import cinescout.rating.data.datasource.LocalPersonalRatingDataSource
import cinescout.rating.data.datasource.RemotePersonalRatingDataSource
import cinescout.rating.domain.model.ScreenplayIdWithPersonalRating
import cinescout.rating.domain.model.ScreenplayPersonalRatingsStoreKey
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
    MutableStore5<ScreenplayPersonalRatingsStoreKey, List<ScreenplayIdWithPersonalRating>, Unit> by Store5Builder
        .from<ScreenplayPersonalRatingsStoreKey, List<ScreenplayIdWithPersonalRating>>(
            fetcher = EitherFetcher.ofOperation { key ->
                require(key is ScreenplayPersonalRatingsStoreKey.Read) { "Only read keys are supported" }
                remoteDataSource.getAllRatingIds(key.type)
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = { key ->
                    require(key is ScreenplayPersonalRatingsStoreKey.Read) { "Only read keys are supported" }
                    localDataSource.findRatingIds(key.type)
                },
                writer = { _, ratings -> localDataSource.insertRatingIds(ratings) }
            )
        )
        .build(
            updater = EitherUpdater.byOperation({ key, _ ->
                require(key is ScreenplayPersonalRatingsStoreKey.Write) { "Only write keys are supported" }
                when (key) {
                    is ScreenplayPersonalRatingsStoreKey.Write.Add ->
                        remoteDataSource.postRating(key.screenplayId, key.rating)
                    is ScreenplayPersonalRatingsStoreKey.Write.Remove ->
                        remoteDataSource.deleteRating(key.screenplayId)
                }
            }),
            bookkeeper = Bookkeeper.empty()
        )
