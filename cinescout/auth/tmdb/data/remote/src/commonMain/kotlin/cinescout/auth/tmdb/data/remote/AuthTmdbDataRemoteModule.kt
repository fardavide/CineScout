package cinescout.auth.tmdb.data.remote

import cinescout.auth.tmdb.data.TmdbAuthRemoteDataSource
import cinescout.auth.tmdb.data.remote.service.TmdbAuthService
import cinescout.network.tmdb.TmdbNetworkQualifier
import org.koin.dsl.module

val AuthTmdbDataRemoteModule = module {

    factory<TmdbAuthRemoteDataSource> { RealTmdbAuthRemoteDataSource(authService = get()) }
    factory {
        TmdbAuthService(
            v3client = get(TmdbNetworkQualifier.V3.Client),
            v4client = get(TmdbNetworkQualifier.V4.Client)
        )
    }
}
