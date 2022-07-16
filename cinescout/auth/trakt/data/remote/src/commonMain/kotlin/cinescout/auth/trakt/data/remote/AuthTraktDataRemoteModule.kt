package cinescout.auth.trakt.data.remote

import cinescout.auth.trakt.data.remote.service.TraktAuthService
import cinescout.network.trakt.TRAKT_CLIENT_ID
import cinescout.network.trakt.TRAKT_CLIENT_SECRET
import cinescout.network.trakt.TraktNetworkQualifier
import org.koin.dsl.module

val AuthTraktDataRemoteModule = module {

    factory {
        TraktAuthService(
            client = get(TraktNetworkQualifier.Client),
            clientId = TRAKT_CLIENT_ID,
            clientSecret = TRAKT_CLIENT_SECRET,
            redirectUrl = TraktRedirectUrl
        )
    }
}

internal const val TraktRedirectUrl = "cinescout://trakt"
