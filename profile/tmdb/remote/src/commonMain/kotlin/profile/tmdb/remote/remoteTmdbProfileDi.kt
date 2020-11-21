package profile.tmdb.remote

import network.tmdb.v4Client
import org.koin.dsl.module
import profile.tmdb.RemoteTmdbProfileSource
import profile.tmdb.tmdbProfileModule

val remoteTmdbProfileModule = module {

    factory { AccountService(client = get(v4Client)) }
    factory<RemoteTmdbProfileSource> { RemoteTmdbProfileSourceImpl(accountService = get()) }

} + tmdbProfileModule
