package cinescout.di.android

import cinescout.design.DesignModule
import cinescout.details.presentation.DetailsPresentationModule
import cinescout.di.kotlin.CineScoutModule
import cinescout.home.presentation.HomePresentationModule
import cinescout.lists.presentation.ListsPresentationModule
import cinescout.search.presentation.SearchPresentationModule
import cinescout.suggestions.presentation.SuggestionsPresentationModule
import org.koin.dsl.module

val CineScoutAndroidModule = module {
    includes(CineScoutModule)

    includes(DesignModule)
    includes(DetailsPresentationModule)
    includes(HomePresentationModule)
    includes(ListsPresentationModule)
    includes(SearchPresentationModule)
    includes(SuggestionsPresentationModule)
}
