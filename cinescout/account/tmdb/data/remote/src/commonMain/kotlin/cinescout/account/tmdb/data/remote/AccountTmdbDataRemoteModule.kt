package cinescout.account.tmdb.data.remote

import cinescout.account.tmdb.data.TmdbAccountRemoteDataSource
import cinescout.network.tmdb.TmdbNetworkQualifier
import org.koin.dsl.module

val AccountTmdbDataRemoteModule = module {

    factory<TmdbAccountRemoteDataSource> { RealTmdbAccountRemoteDataSource(service = get()) }
    factory { TmdbAccountService(authProvider = get(), client = get(TmdbNetworkQualifier.V3.Client)) }
}
