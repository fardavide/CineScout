package cinescout.network.trakt

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
@ComponentScan
class NetworkTraktModule {

    @Single
    @TraktNetworkQualifier.Client
    fun cineScoutTraktClient(authProvider: TraktAuthProvider, refreshAccessToken: RefreshTraktAccessToken) =
        CineScoutTraktClient(authProvider = authProvider, refreshAccessToken = refreshAccessToken)

    @Single
    @TraktNetworkQualifier.RefreshTokenClient
    fun cineScoutTraktRefreshTokenClient() = CineScoutTraktRefreshTokenClient()
}

object TraktNetworkQualifier {

    @Named("Trakt client")
    annotation class Client

    @Named("Trakt client id")
    annotation class ClientId

    @Named("Trakt client secret")
    annotation class ClientSecret

    @Named("Trakt redirect url")
    annotation class RedirectUrl

    @Named("Trakt refresh token client")
    annotation class RefreshTokenClient
}
