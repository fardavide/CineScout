package cinescout.auth.trakt.data.remote

import cinescout.network.trakt.TRAKT_CLIENT_ID
import cinescout.network.trakt.TRAKT_CLIENT_SECRET
import cinescout.network.trakt.TraktClientId
import cinescout.network.trakt.TraktClientSecret
import cinescout.network.trakt.TraktRedirectUrl
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named

@Module
@ComponentScan
class AuthDataRemoteModule {

    @Factory
    @Named(TraktClientId)
    fun clientId() = TRAKT_CLIENT_ID

    @Factory
    @Named(TraktClientSecret)
    fun clientSecret() = TRAKT_CLIENT_SECRET

    @Factory
    @Named(TraktRedirectUrl)
    fun redirectUrl() = TraktRedirectUrlString
}

internal const val TraktRedirectUrlString = "cinescout://trakt"

@Suppress("MaxLineLength")
internal const val TraktAuthorizeAppUrl =
    "https://api.trakt.tv/oauth/authorize?response_type=code&client_id=$TRAKT_CLIENT_ID&redirect_uri=$TraktRedirectUrlString"
