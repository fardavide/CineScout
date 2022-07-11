package cinescout.network.tmdb

import org.koin.core.qualifier.named
import org.koin.dsl.module

val NetworkTmdbModule = module {

    single(TmdbNetworkQualifier.V3.Client) { CineScoutTmdbV3Client(authProvider = get()) }
    single(TmdbNetworkQualifier.V4.Client) { CineScoutTmdbV4Client(authProvider = get()) }
}

object TmdbNetworkQualifier {

    object V3 {

        val Client = named("Tmdb client v3")
    }

    object V4 {

        val Client = named("Tmdb client v4")
    }
}
