package entities

import co.touchlab.kermit.Kermit
import org.koin.dsl.module

val entitiesModule = module {

    single { Kermit(logger = get()) }
}
