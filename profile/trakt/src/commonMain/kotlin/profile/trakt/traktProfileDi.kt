package profile.trakt

import org.koin.dsl.module
import profile.TraktProfileRepository
import profile.profileModule

val traktProfileModule = module {

    factory<TraktProfileRepository> { TraktProfileRepositoryImpl(localSource = get(), remoteSource = get()) }

} + profileModule
