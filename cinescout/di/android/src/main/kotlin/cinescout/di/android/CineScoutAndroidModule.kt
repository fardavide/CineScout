package cinescout.di.android

import cinescout.di.kotlin.CineScoutModule
import cinescout.home.presentation.HomePresentationModule
import org.koin.dsl.module

val CineScoutAndroidModule = module {
    includes(CineScoutModule)

    includes(HomePresentationModule)
}
