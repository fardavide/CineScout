package entities

import co.touchlab.kermit.Kermit
import org.koin.dsl.module

const val TmdbOauthCallback = "oauth-tmdb://"

val entitiesModule = module {

    single { Kermit(logger = get()) }
}
