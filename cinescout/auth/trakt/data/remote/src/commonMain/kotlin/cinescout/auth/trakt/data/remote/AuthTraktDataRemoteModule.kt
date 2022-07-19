package cinescout.auth.trakt.data.remote

import cinescout.auth.trakt.data.TraktAuthRemoteDataSource
import cinescout.auth.trakt.data.remote.service.TraktAuthService
import cinescout.network.trakt.TRAKT_CLIENT_ID
import cinescout.network.trakt.TRAKT_CLIENT_SECRET
import cinescout.network.trakt.TraktNetworkQualifier
import org.koin.dsl.module

val AuthTraktDataRemoteModule = module {

    factory<TraktAuthRemoteDataSource> { RealTraktAuthRemoteDataSource(authService = get()) }
    factory {
        TraktAuthService(
            client = get(TraktNetworkQualifier.Client),
            clientId = TRAKT_CLIENT_ID,
            clientSecret = TRAKT_CLIENT_SECRET,
            redirectUrl = TraktRedirectUrl
        )
    }
}

internal const val TraktRedirectUrl = "cinescout://trakt"

@Suppress("MaxLineLength")
internal const val TraktAuthorizeAppUrl =
    "https://api.trakt.tv/oauth/authorize?response_type=code&client_id=$TRAKT_CLIENT_ID&redirect_uri=$TraktRedirectUrl"
