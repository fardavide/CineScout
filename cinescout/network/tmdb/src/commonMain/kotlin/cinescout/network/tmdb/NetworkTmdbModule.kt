package cinescout.network.tmdb

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
@ComponentScan
class NetworkTmdbModule {

    @Single
    @Named(TmdbNetworkQualifier.V3.Client)
    fun cineScoutTmdbV3Client(authProvider: TmdbAuthProvider) = CineScoutTmdbV3Client(authProvider = authProvider)

    @Single
    @Named(TmdbNetworkQualifier.V4.Client)
    fun cineScoutTmdbV4Client(authProvider: TmdbAuthProvider) = CineScoutTmdbV4Client(authProvider = authProvider)
}

object TmdbNetworkQualifier {

    const val RedirectUrl = "Tmdb redirect url"

    object V3 {

        const val Client = "Tmdb client v3"
    }

    object V4 {

        const val Client = "Tmdb client v4"
    }
}
