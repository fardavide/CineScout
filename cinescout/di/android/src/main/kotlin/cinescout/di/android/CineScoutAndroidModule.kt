package cinescout.di.android

import cinescout.account.presentation.AccountPresentationModule
import cinescout.design.DesignModule
import cinescout.details.presentation.DetailsPresentationModule
import cinescout.di.kotlin.CineScoutModule
import cinescout.home.presentation.HomePresentationModule
import cinescout.lists.presentation.ListsPresentationModule
import cinescout.media.presentation.MediaPresentationModule
import cinescout.profile.presentation.ProfilePresentationModule
import cinescout.search.presentation.SearchPresentationModule
import cinescout.suggestions.presentation.SuggestionsPresentationModule
import cinescout.utils.compose.UtilsComposeModule
import org.koin.dsl.module
import org.koin.ksp.generated.module

val CineScoutAndroidModule = module {
    includes(CineScoutModule)

    includes(AccountPresentationModule().module)
    includes(DesignModule().module)
    includes(DetailsPresentationModule().module)
    includes(HomePresentationModule().module)
    includes(ListsPresentationModule().module)
    includes(MediaPresentationModule().module)
    includes(ProfilePresentationModule().module)
    includes(SearchPresentationModule().module)
    includes(SuggestionsPresentationModule)
    includes(UtilsComposeModule().module)
}
