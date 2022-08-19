package cinescout.store

import cinescout.utils.kotlin.DispatcherQualifier
import org.koin.dsl.module
import store.RealStoreOwner
import store.StoreOwner

val StoreModule = module {
    factory { StoreFetchDataRepository(ioDispatcher = get(DispatcherQualifier.Io), queries = get()) }
    single<StoreOwner> {
        val fetchDataRepository: StoreFetchDataRepository = get()
        RealStoreOwner(
            getFetchData = { key -> fetchDataRepository.getFetchData(key) },
            saveFetchData = { key, fetchData -> fetchDataRepository.saveFetchData(key, fetchData) }
        )
    }
}
