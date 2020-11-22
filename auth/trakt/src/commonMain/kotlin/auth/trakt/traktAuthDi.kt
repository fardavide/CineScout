package auth.trakt

import auth.credentials.authCredentialsModule
import entities.auth.TraktAuth
import network.trakt.client
import network.trakt.clientId
import network.trakt.clientSecret
import network.trakt.traktNetworkModule
import org.koin.dsl.module

val traktAuthModule = module {

    factory<TraktAuth> { TraktAuthImpl(authService = get()) }

    factory { AuthService(client = get(client), clientId = get(clientId), clientSecret = get(clientSecret)) }

} + traktNetworkModule + authCredentialsModule
