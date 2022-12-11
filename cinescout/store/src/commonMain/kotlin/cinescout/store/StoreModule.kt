package cinescout.store

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import store.RealStoreOwner
import store.StoreOwner

@Module
@ComponentScan
class StoreModule {

    @Single
    internal fun storeOwner(
        fetchDataRepository: StoreFetchDataRepository
    ): StoreOwner = RealStoreOwner(
        getFetchData = { key -> fetchDataRepository.getFetchData(key) },
        saveFetchData = { key, fetchData -> fetchDataRepository.saveFetchData(key, fetchData) }
    )
}
