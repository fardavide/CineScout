package auth.tmdb

import entities.auth.TmdbAuth
import network.tmdb.tmdbNetworkModule
import org.koin.dsl.module

val tmdbAuthModule = module {

    factory<TmdbAuth> { TmdbAuthImpl() }

} + tmdbNetworkModule
