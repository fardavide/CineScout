package cinescout.rating.data.store

import cinescout.rating.data.datasource.LocalPersonalRatingDataSource
import cinescout.rating.data.datasource.RemotePersonalRatingDataSource
import cinescout.rating.domain.model.PersonalRatingsStoreKey
import cinescout.rating.domain.model.ScreenplayIdWithPersonalRating
import cinescout.rating.domain.store.PersonalRatingIdsStore
import cinescout.store5.EitherFetcher
import cinescout.store5.EitherUpdater
import cinescout.store5.MutableStore5
import cinescout.store5.MutableStore5Builder
import org.koin.core.annotation.Single
import org.mobilenativefoundation.store.store5.SourceOfTruth

@Single(binds = [PersonalRatingIdsStore::class])
internal class RealPersonalRatingIdsStore(
    private val localDataSource: LocalPersonalRatingDataSource,
    private val remoteDataSource: RemotePersonalRatingDataSource
) : PersonalRatingIdsStore,
    MutableStore5<PersonalRatingsStoreKey, List<ScreenplayIdWithPersonalRating>, Unit> by MutableStore5Builder
        .from<PersonalRatingsStoreKey, List<ScreenplayIdWithPersonalRating>>(
            fetcher = EitherFetcher.ofOperation { key ->
                require(key is PersonalRatingsStoreKey.Read) { "Only read keys are supported" }
                remoteDataSource.getAllRatingIds(key.type)
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = { key ->
                    require(key is PersonalRatingsStoreKey.Read) { "Only read keys are supported" }
                    localDataSource.findRatingIds(key.type)
                },
                writer = { key, ratings ->
                    when (key) {
                        is PersonalRatingsStoreKey.Read -> localDataSource.updateAllRatingIds(ratings)
                        is PersonalRatingsStoreKey.Write.Add -> localDataSource.insert(key.screenplayIds, key.rating)
                        is PersonalRatingsStoreKey.Write.Remove -> localDataSource.delete(key.screenplayId)
                    }
                }
            )
        )
        .build(
            updater = EitherUpdater.byOperation({ key, _ ->
                require(key is PersonalRatingsStoreKey.Write) { "Only write keys are supported" }
                when (key) {
                    is PersonalRatingsStoreKey.Write.Add ->
                        remoteDataSource.postRating(key.screenplayIds.tmdb, key.rating)
                    is PersonalRatingsStoreKey.Write.Remove ->
                        remoteDataSource.deleteRating(key.screenplayId)
                }
            })
        )
