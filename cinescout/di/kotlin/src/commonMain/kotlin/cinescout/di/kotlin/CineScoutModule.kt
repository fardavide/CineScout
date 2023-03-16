package cinescout.di.kotlin

import cinescout.KotlinUtilsModule
import cinescout.account.domain.AccountDomainModule
import cinescout.account.trakt.data.AccountTraktDataModule
import cinescout.account.trakt.data.local.AccountTraktDataLocalModule
import cinescout.account.trakt.data.remote.AccountTraktDataRemoteModule
import cinescout.auth.domain.AuthDomainModule
import cinescout.auth.trakt.data.AuthDataModule
import cinescout.auth.trakt.data.local.AuthDataLocalModule
import cinescout.auth.trakt.data.remote.AuthDataRemoteModule
import cinescout.database.DatabaseModule
import cinescout.details.domain.DetailsDomainModule
import cinescout.network.NetworkModule
import cinescout.network.tmdb.NetworkTmdbModule
import cinescout.network.trakt.NetworkTraktModule
import cinescout.people.data.PeopleDataModule
import cinescout.people.data.local.PeopleDataLocalModule
import cinescout.people.data.remote.PeopleDataRemoteModule
import cinescout.people.domain.PeopleDomainModule
import cinescout.rating.data.RatingDataModule
import cinescout.rating.data.local.RatingDataLocalModule
import cinescout.rating.data.remote.RatingDataRemoteModule
import cinescout.rating.domain.RatingDomainModule
import cinescout.screenplay.data.ScreenplayDataModule
import cinescout.screenplay.data.local.ScreenplayDataLocalModule
import cinescout.screenplay.data.remote.ScreenplayDataRemoteModule
import cinescout.screenplay.data.remote.tmdb.ScreenplayDataRemoteTmdbModule
import cinescout.screenplay.domain.ScreenplayDomainModule
import cinescout.search.domain.SearchDomainModule
import cinescout.settings.data.SettingsDataModule
import cinescout.settings.data.local.SettingsDataLocalModule
import cinescout.settings.domain.SettingsDomainModule
import cinescout.suggestions.data.SuggestionsDataModule
import cinescout.suggestions.data.local.SuggestionsDataLocalModule
import cinescout.suggestions.domain.SuggestionsDomainModule
import cinescout.utils.kotlin.DispatcherQualifier
import cinescout.voting.data.VotingDataModule
import cinescout.voting.domain.VotingDomainModule
import cinescout.watchlist.data.WatchlistDataModule
import cinescout.watchlist.data.local.WatchlistDataLocalModule
import cinescout.watchlist.data.remote.WatchlistDataRemoteModule
import cinescout.watchlist.domain.WatchlistDomainModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ksp.generated.module
import screenplay.data.remote.trakt.ScreenplayDataRemoteTraktModule

val CineScoutModule = module {
    includes(
        AccountDomainModule().module,
        AccountTraktDataModule().module,
        AccountTraktDataLocalModule().module,
        AccountTraktDataRemoteModule().module,

        AuthDataModule().module,
        AuthDataLocalModule().module,
        AuthDataRemoteModule().module,
        AuthDomainModule().module,

        DatabaseModule().module,

        DetailsDomainModule().module,

        KotlinUtilsModule().module,

        NetworkModule().module,
        NetworkTmdbModule().module,
        NetworkTraktModule().module,

        PeopleDataModule().module,
        PeopleDataLocalModule().module,
        PeopleDataRemoteModule().module,
        PeopleDomainModule().module,

        RatingDataModule().module,
        RatingDataLocalModule().module,
        RatingDataRemoteModule().module,
        RatingDomainModule().module,

        ScreenplayDataModule().module,
        ScreenplayDataLocalModule().module,
        ScreenplayDataRemoteModule().module,
        ScreenplayDataRemoteTmdbModule().module,
        ScreenplayDataRemoteTraktModule().module,
        ScreenplayDomainModule().module,

        SearchDomainModule().module,

        SettingsDataModule().module,
        SettingsDataLocalModule().module,
        SettingsDomainModule().module,

        SuggestionsDataModule().module,
        SuggestionsDataLocalModule().module,
        SuggestionsDomainModule().module,

        VotingDataModule().module,
        VotingDomainModule().module,

        WatchlistDataModule().module,
        WatchlistDataLocalModule().module,
        WatchlistDataRemoteModule().module,
        WatchlistDomainModule().module
    )

    factory(named(DispatcherQualifier.Io)) { Dispatchers.IO }
    single<CoroutineDispatcher>(named(DispatcherQualifier.DatabaseWrite)) {
        @OptIn(DelicateCoroutinesApi::class)
        newSingleThreadContext("Database write")
    }
}

val AppVersionQualifier = named(cinescout.AppVersionQualifier)
