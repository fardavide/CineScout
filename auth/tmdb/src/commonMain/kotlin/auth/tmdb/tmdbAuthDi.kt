package auth.tmdb

import auth.credentials.authCredentialsModule
import auth.tmdb.auth.AuthService
import domain.auth.GetTmdbAccessToken
import domain.auth.GetTmdbAccountId
import domain.auth.GetTmdbSessionId
import entities.auth.TmdbAuth
import network.tmdb.accountId
import network.tmdb.sessionId
import network.tmdb.tmdbNetworkModule
import network.tmdb.v4Client
import network.tmdb.v4accessToken
import org.koin.dsl.module

val tmdbAuthModule = module {

    factory<TmdbAuth> { TmdbAuthImpl(authService = get()) }

    factory { AuthService(client = get(v4Client), storeCredentials = get()) }
    factory(v4accessToken) { get<GetTmdbAccessToken>().blocking() ?: "" }
    factory(accountId) { get<GetTmdbAccountId>().blocking()?.s ?: "" }
    factory(sessionId) { get<GetTmdbSessionId>().blocking() ?: "" }

} + tmdbNetworkModule + authCredentialsModule
