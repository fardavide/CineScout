package cinescout.auth.tmdb.data.remote

import cinescout.auth.tmdb.data.TmdbAuthRemoteDataSource
import cinescout.auth.tmdb.data.model.TmdbRequestToken
import cinescout.auth.tmdb.data.remote.service.TmdbAuthService
import cinescout.network.tmdb.TmdbNetworkQualifier
import org.koin.dsl.module

val AuthTmdbDataRemoteModule = module {

    factory<TmdbAuthRemoteDataSource> { RealTmdbAuthRemoteDataSource(authService = get()) }
    factory {
        TmdbAuthService(
            redirectUrl = TmdbRedirectUrl,
            v3client = get(TmdbNetworkQualifier.V3.Client),
            v4client = get(TmdbNetworkQualifier.V4.Client)
        )
    }
}

internal const val TmdbRedirectUrl = "cinescout://tmdb"

internal fun getTmdbAuthorizeTokenUrl(requestToken: TmdbRequestToken) =
    "https://www.themoviedb.org/auth/access?request_token=${requestToken.value}"


