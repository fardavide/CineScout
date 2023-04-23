package cinescout.auth.trakt.data.remote

import cinescout.network.trakt.TRAKT_CLIENT_ID
import cinescout.network.trakt.TRAKT_CLIENT_SECRET
import cinescout.network.trakt.TraktNetworkQualifier
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
@ComponentScan
class AuthDataRemoteModule {

    @Factory
    @TraktNetworkQualifier.ClientId
    fun clientId() = TRAKT_CLIENT_ID

    @Factory
    @TraktNetworkQualifier.ClientSecret
    fun clientSecret() = TRAKT_CLIENT_SECRET

    @Factory
    @TraktNetworkQualifier.RedirectUrl
    fun redirectUrl() = TraktRedirectUrl
}

internal const val TraktRedirectUrl = "cinescout://trakt"

@Suppress("MaxLineLength")
internal const val TraktAuthorizeAppUrl =
    "https://api.trakt.tv/oauth/authorize?response_type=code&client_id=$TRAKT_CLIENT_ID&redirect_uri=$TraktRedirectUrl"
