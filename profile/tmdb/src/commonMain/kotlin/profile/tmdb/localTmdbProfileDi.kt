package profile.tmdb

import org.koin.dsl.module
import profile.TmdbProfileRepository

val localTmdbProfileModule = module {
    factory<TmdbProfileRepository> { TmdbProfileRepositoryImpl(localRepository = get(), remoteRepository = get()) }
}
