package cinescout.network.tmdb

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
@ComponentScan
class NetworkTmdbModule {

    @Single
    @Named(TmdbNetworkQualifier.Client)
    fun cineScoutTmdbV3Client(authProvider: TmdbAuthProvider) =
        CineScoutTmdbClient(authProvider = authProvider)
}

object TmdbNetworkQualifier {

    const val Client = "Tmdb client"
}
