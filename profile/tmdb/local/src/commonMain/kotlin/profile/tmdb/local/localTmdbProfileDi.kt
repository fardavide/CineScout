package profile.tmdb.local

import database.Database
import org.koin.dsl.module
import profile.tmdb.LocalTmdbProfileSource
import profile.tmdb.tmdbProfileModule

val localTmdbProfileModule = module {

    factory<LocalTmdbProfileSource> { LocalTmdbProfileSourceImpl(profiles = get()) }

    factory { get<Database>().profileQueries }

} + tmdbProfileModule
