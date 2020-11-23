package profile.trakt.local

import database.Database
import org.koin.dsl.module
import profile.trakt.LocalTraktProfileSource
import profile.trakt.traktProfileModule

val localTraktProfileModule = module {

    factory<LocalTraktProfileSource> { LocalTrackProfleSourceImpl(profiles = get()) }

    factory { get<Database>().traktProfileQueries }

} + traktProfileModule
