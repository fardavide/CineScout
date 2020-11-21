package auth.tmdb

import auth.credentials.authCredentialsModule
import auth.tmdb.auth.AuthService
import domain.auth.GetTmdbAccessToken
import domain.auth.GetTmdbSessionId
import domain.auth.GetTmdbV3accountId
import domain.auth.GetTmdbV4accountId
import entities.auth.TmdbAuth
import network.tmdb.sessionId
import network.tmdb.tmdbNetworkModule
import network.tmdb.v3accountId
import network.tmdb.v4Client
import network.tmdb.v4accessToken
import network.tmdb.v4accountId
import org.koin.dsl.module

val tmdbAuthModule = module {

    factory<TmdbAuth> { TmdbAuthImpl(authService = get()) }

    factory {
        AuthService(
            client = get(v4Client),
            storeCredentials = get(),
            storeTmdbAccountId = get(),
            getProfile = get()
        )
    }
    factory(v4accessToken) { get<GetTmdbAccessToken>().blocking() ?: "" }
    factory(v3accountId) { get<GetTmdbV3accountId>().blocking()?.i?.toString() ?: "" }
    factory(v4accountId) { get<GetTmdbV4accountId>().blocking()?.s ?: "" }
    factory(sessionId) { get<GetTmdbSessionId>().blocking() ?: "" }

} + tmdbNetworkModule + authCredentialsModule

val testTmdbAuthModule = module(override = true) {
    factory(v4accessToken) { "" }
    factory(v3accountId) { "" }
    factory(v4accountId) { "" }
    factory(sessionId) { "" }
}
