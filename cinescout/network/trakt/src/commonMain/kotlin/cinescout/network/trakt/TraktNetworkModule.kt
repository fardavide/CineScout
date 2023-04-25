package cinescout.network.trakt

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
@ComponentScan
class NetworkTraktModule {

    @Single
    @Named(TraktClient)
    fun cineScoutTraktClient(authProvider: TraktAuthProvider, refreshAccessToken: RefreshTraktAccessToken) =
        CineScoutTraktClient(authProvider = authProvider, refreshAccessToken = refreshAccessToken)

    @Single
    @Named(TraktRefreshTokenClient)
    fun cineScoutTraktRefreshTokenClient() = CineScoutTraktRefreshTokenClient()
}

const val TraktClient = "Trakt client"

const val TraktClientId = "Trakt client id"

const val TraktClientSecret = "Trakt client secret"

const val TraktRedirectUrl = "Trakt redirect url"

const val TraktRefreshTokenClient = "Trakt refresh token client"
