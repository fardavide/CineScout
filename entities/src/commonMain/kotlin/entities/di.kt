package entities

import co.touchlab.kermit.Kermit
import org.koin.dsl.module

const val TmdbOauthCallback = "cinescout://tmdb-token"

val entitiesModule = module {

    single { Kermit(logger = get()) }
}
