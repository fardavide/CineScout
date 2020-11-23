package auth.trakt

import auth.credentials.authCredentialsModule
import domain.auth.GetTraktAccessToken
import entities.auth.TraktAuth
import network.trakt.accessToken
import network.trakt.client
import network.trakt.clientId
import network.trakt.clientSecret
import network.trakt.traktNetworkModule
import org.koin.dsl.module

val traktAuthModule = module {

    factory<TraktAuth> { TraktAuthImpl(authService = get()) }

    factory {
        AuthService(
            client = get(client),
            clientId = get(clientId),
            clientSecret = get(clientSecret),
            storeAccessToken = get()
        )
    }
    factory(accessToken) { get<GetTraktAccessToken>().blocking() ?: "" }

} + traktNetworkModule + authCredentialsModule
