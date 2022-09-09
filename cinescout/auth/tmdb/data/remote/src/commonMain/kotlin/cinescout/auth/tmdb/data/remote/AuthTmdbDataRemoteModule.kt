package cinescout.auth.tmdb.data.remote

import cinescout.auth.tmdb.data.TmdbAuthRemoteDataSource
import cinescout.auth.tmdb.data.model.TmdbRequestToken
import cinescout.auth.tmdb.data.remote.service.TmdbAuthService
import cinescout.auth.tmdb.domain.usecase.IsTmdbLinked
import cinescout.network.NetworkQualifier
import cinescout.network.tmdb.TmdbNetworkQualifier
import kotlinx.coroutines.flow.first
import org.koin.dsl.module

val AuthTmdbDataRemoteModule = module {

    factory(NetworkQualifier.IsFirstSourceLinked) { suspend { get<IsTmdbLinked>().invoke().first() } }
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


