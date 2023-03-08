package cinescout.screenplay.data.repository

import cinescout.screenplay.data.datasource.LocalScreenplayDataSource
import cinescout.screenplay.data.datasource.RemoteScreenplayDataSource
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.screenplay.domain.repository.ScreenplayRepository
import cinescout.utils.kotlin.plus
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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

    override fun getRecommended(refresh: Refresh): Store<List<Screenplay>> {
        TODO("Not yet implemented")
    }

    override fun getRecommendedIds(refresh: Refresh): Store<List<TmdbScreenplayId>> = Store(
        key = StoreKey<TmdbScreenplayId>("recommended"),
        refresh = refresh,
        fetcher = Fetcher.forOperation {
            coroutineScope {
                val moviesDeferred = async { remoteDataSource.getRecommendedMovies() }
                val tvShowsDeferred = async { remoteDataSource.getRecommendedTvShows() }
                moviesDeferred.await() + tvShowsDeferred.await()
            }
        },
        reader = Reader.fromSource(localDataSource.findRecommendedIds()),
        write = { localDataSource.insertRecommendedIds(it) }
    )
}
