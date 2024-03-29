package cinescout.di.android

import cinescout.account.presentation.AccountPresentationModule
import cinescout.details.presentation.DetailsPresentationModule
import cinescout.di.kotlin.CineScoutModule
import cinescout.home.presentation.HomePresentationModule
import cinescout.lists.presentation.ListsPresentationModule
import cinescout.media.presentation.MediaPresentationModule
import cinescout.notification.NotificationModule
import cinescout.performance.android.PerformanceAndroidModule
import cinescout.profile.presentation.ProfilePresentationModule
import cinescout.report.presentation.ReportPresentationModule
import cinescout.search.presentation.SearchPresentationModule
import cinescout.settings.presentation.SettingsPresentationModule
import cinescout.suggestions.presentation.SuggestionsPresentationModule
import cinescout.sync.automated.SyncAutomatedModule
import cinescout.utils.android.UtilsAndroidModule
import cinescout.utils.compose.UtilsComposeModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module(
    includes = [
        CineScoutModule::class,

        AccountPresentationModule::class,
        DetailsPresentationModule::class,
        HomePresentationModule::class,
        ListsPresentationModule::class,
        MediaPresentationModule::class,
        NotificationModule::class,
        PerformanceAndroidModule::class,
        ProfilePresentationModule::class,
        ReportPresentationModule::class,
        SearchPresentationModule::class,
        SettingsPresentationModule::class,
        SuggestionsPresentationModule::class,
        SyncAutomatedModule::class,
        UtilsAndroidModule::class,
        UtilsComposeModule::class
    ]
)
@ComponentScan
class CineScoutAndroidModule

@Factory internal class Empty
