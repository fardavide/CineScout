package entities

import co.touchlab.kermit.Kermit
import org.koin.dsl.module

const val TmdbOauthCallback = "cinescout://tmdb"
const val TraktOauthCallback = "cinescout://trakt"

val entitiesModule = module {

    single { Kermit(logger = get()) }
}
