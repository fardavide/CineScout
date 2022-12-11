package cinescout.auth.trakt.data.remote

import cinescout.auth.trakt.domain.usecase.IsTraktLinked
import cinescout.network.NetworkQualifier
import cinescout.network.trakt.TRAKT_CLIENT_ID
import cinescout.network.trakt.TRAKT_CLIENT_SECRET
import cinescout.network.trakt.TraktNetworkQualifier
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named

@Module
@ComponentScan
class AuthTraktDataRemoteModule {

    @Factory
    @Named(TraktNetworkQualifier.ClientId)
    fun clientId() = TRAKT_CLIENT_ID

    @Factory
    @Named(TraktNetworkQualifier.ClientSecret)
    fun clientSecret() = TRAKT_CLIENT_SECRET

    @Factory
    @Named(NetworkQualifier.IsSecondSourceLinked)
    fun isSecondSourceLinked(isTraktLinked: IsTraktLinked) = suspend { isTraktLinked().first() }

    @Factory
    @Named(TraktNetworkQualifier.RedirectUrl)
    fun redirectUrl() = TraktRedirectUrl
}

internal const val TraktRedirectUrl = "cinescout://trakt"

@Suppress("MaxLineLength")
internal const val TraktAuthorizeAppUrl =
    "https://api.trakt.tv/oauth/authorize?response_type=code&client_id=$TRAKT_CLIENT_ID&redirect_uri=$TraktRedirectUrl"
