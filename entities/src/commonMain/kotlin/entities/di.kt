package entities

import co.touchlab.kermit.Kermit
import org.koin.dsl.module

const val TmdbOauthCallback = "cinescout://tmdb-token"
const val TraktOauthCallback = "cinescout://app"

val entitiesModule = module {

    single { Kermit(logger = get()) }
}
