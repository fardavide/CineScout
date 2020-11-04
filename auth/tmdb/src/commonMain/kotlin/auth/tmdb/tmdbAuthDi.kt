package auth.tmdb

import auth.credentials.authCredentialsModule
import auth.tmdb.auth.AuthService
import domain.auth.GetTmdbAccessToken
import entities.auth.TmdbAuth
import network.tmdb.tmdbNetworkModule
import network.tmdb.v4Client
import network.tmdb.v4accessToken
import org.koin.dsl.module

val tmdbAuthModule = module {

    factory<TmdbAuth> { TmdbAuthImpl(authService = get()) }

    factory { AuthService(client = get(v4Client), storeToken = get()) }
    factory(v4accessToken) { get<GetTmdbAccessToken>().blocking() ?: "no token" }

} + tmdbNetworkModule + authCredentialsModule
