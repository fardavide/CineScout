package profile.tmdb

import org.koin.dsl.module
import profile.TmdbProfileRepository
import profile.profileModule

val tmdbProfileModule = module {

    factory<TmdbProfileRepository> { TmdbProfileRepositoryImpl(localSource = get(), remoteSource = get()) }
} + profileModule
