package auth.tmdb

import auth.tmdb.auth.AuthService
import entities.auth.TmdbAuth
import network.tmdb.tmdbNetworkModule
import network.tmdb.v4Client
import org.koin.dsl.module

val tmdbAuthModule = module {

    factory<TmdbAuth> { TmdbAuthImpl(authService = get()) }

    factory { AuthService(client = get(v4Client)) }

} + tmdbNetworkModule
