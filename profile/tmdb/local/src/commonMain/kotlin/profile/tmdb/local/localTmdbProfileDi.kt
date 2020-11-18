package profile.tmdb.local

import org.koin.dsl.module
import profile.tmdb.LocalTmdbProfileSource
import profile.tmdb.tmdbProfileModule

val localTmdbProfiileModule = module {

    factory<LocalTmdbProfileSource> { LocalTmdbProfileSourceImpl(profiles = get()) }
} + tmdbProfileModule
