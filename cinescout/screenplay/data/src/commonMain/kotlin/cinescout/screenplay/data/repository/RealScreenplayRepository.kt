package cinescout.screenplay.data.repository

import cinescout.screenplay.data.datasource.LocalScreenplayDataSource
import cinescout.screenplay.data.datasource.RemoteScreenplayDataSource
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.repository.ScreenplayRepository
import org.koin.core.annotation.Factory
import store.Fetcher
import store.Reader
import store.Refresh
import store.Store
import store.StoreKey
import store.StoreOwner

@Factory(binds = [ScreenplayRepository::class])
class RealScreenplayRepository(
    private val localDataSource: LocalScreenplayDataSource,
    private val remoteDataSource: RemoteScreenplayDataSource,
    storeOwner: StoreOwner
) : ScreenplayRepository, StoreOwner by storeOwner {

    override fun getRecommendedIds(refresh: Refresh): Store<List<TmdbScreenplayId>> = Store(
        key = StoreKey<TmdbScreenplayId>("recommended"),
        refresh = refresh,
        fetcher = Fetcher.forOperation { remoteDataSource.getRecommended() },
        reader = Reader.fromSource(localDataSource.findRecommendedIds()),
        write = { localDataSource.insertRecommendedIds(it) }
    )
}
