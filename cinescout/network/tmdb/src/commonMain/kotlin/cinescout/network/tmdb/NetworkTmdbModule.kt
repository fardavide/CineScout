package cinescout.network.tmdb

import org.koin.core.qualifier.named
import org.koin.dsl.module

val NetworkTmdbModule = module {

    single(TmdbNetworkQualifier.V3.Client) { CineScoutV3Client() }
    single(TmdbNetworkQualifier.V4.Client) { CineScoutV4Client() }
}

object TmdbNetworkQualifier {

    val SessionId = named("Tmdb current session Id")

    object V3 {

        val Client = named("Tmdb client v3")
        val AccountId = named("Tmdb V3 current account Id")
    }

    object V4 {

        val Client = named("Tmdb client v4")
        val AccessToken = named("Tmdb v4 access token")
        val AccountId = named("Tmdb V4 current account Id")
    }
}
