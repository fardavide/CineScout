package cinescout.network.trakt

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
@ComponentScan
class NetworkTraktModule {

    @Single
    @Named(TraktNetworkQualifier.Client)
    fun cineScoutTraktClient(authProvider: TraktAuthProvider, refreshAccessToken: RefreshTraktAccessToken) =
        CineScoutTraktClient(authProvider = authProvider, refreshAccessToken = refreshAccessToken)
}

object TraktNetworkQualifier {

    const val Client = "Trakt client"
    const val ClientId = "Trakt client id"
    const val ClientSecret = "Trakt client secret"
    const val RedirectUrl = "Trakt redirect url"
}
