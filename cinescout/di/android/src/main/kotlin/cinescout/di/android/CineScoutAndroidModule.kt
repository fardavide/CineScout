package cinescout.di.android

import cinescout.design.DesignModule
import cinescout.di.kotlin.CineScoutModule
import cinescout.home.presentation.HomePresentationModule
import cinescout.suggestions.SuggestionsPresentationModule
import org.koin.dsl.module

val CineScoutAndroidModule = module {
    includes(CineScoutModule)

    includes(DesignModule)
    includes(HomePresentationModule)
    includes(SuggestionsPresentationModule)
}
