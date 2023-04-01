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
import cinescout.fetchdata.data.FetchDataDataModule
import cinescout.media.data.MediaDataModule
import cinescout.media.data.local.MediaDataLocalModule
import cinescout.media.data.remote.MediaDataRemoteModule
import cinescout.media.domain.MediaDomainModule
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
import cinescout.search.data.SearchDataModule
import cinescout.search.data.local.SearchDataLocalModule
import cinescout.search.data.remote.SearchDataRemoteModule
import cinescout.search.domain.SearchDomainModule
import cinescout.settings.data.SettingsDataModule
import cinescout.settings.data.local.SettingsDataLocalModule
import cinescout.settings.domain.SettingsDomainModule
import cinescout.suggestions.data.SuggestionsDataModule
import cinescout.suggestions.data.local.SuggestionsDataLocalModule
import cinescout.suggestions.domain.SuggestionsDomainModule
import cinescout.utils.kotlin.DatabaseWriteDispatcher
import cinescout.utils.kotlin.IoDispatcher
import cinescout.voting.data.VotingDataModule
import cinescout.voting.domain.VotingDomainModule
import cinescout.watchlist.data.WatchlistDataModule
import cinescout.watchlist.data.local.WatchlistDataLocalModule
import cinescout.watchlist.data.remote.WatchlistDataRemoteModule
import cinescout.watchlist.domain.WatchlistDomainModule
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import screenplay.data.remote.trakt.ScreenplayDataRemoteTraktModule

@ComponentScan
@Module(
    includes = [
        AccountDomainModule::class,
        AccountTraktDataModule::class,
        AccountTraktDataLocalModule::class,
        AccountTraktDataRemoteModule::class,

        AuthDataModule::class,
        AuthDataLocalModule::class,
        AuthDataRemoteModule::class,
        AuthDomainModule::class,

        DatabaseModule::class,

        DetailsDomainModule::class,

        FetchDataDataModule::class,

        KotlinUtilsModule::class,

        MediaDataModule::class,
        MediaDataLocalModule::class,
        MediaDataRemoteModule::class,
        MediaDomainModule::class,

        NetworkModule::class,
        NetworkTmdbModule::class,
        NetworkTraktModule::class,

        PeopleDataModule::class,
        PeopleDataLocalModule::class,
        PeopleDataRemoteModule::class,
        PeopleDomainModule::class,

        RatingDataModule::class,
        RatingDataLocalModule::class,
        RatingDataRemoteModule::class,
        RatingDomainModule::class,

        ScreenplayDataModule::class,
        ScreenplayDataLocalModule::class,
        ScreenplayDataRemoteModule::class,
        ScreenplayDataRemoteTraktModule::class,
        ScreenplayDataRemoteTmdbModule::class,
        ScreenplayDomainModule::class,

        SearchDataModule::class,
        SearchDataLocalModule::class,
        SearchDataRemoteModule::class,
        SearchDomainModule::class,

        SettingsDataModule::class,
        SettingsDataLocalModule::class,
        SettingsDomainModule::class,

        SuggestionsDataModule::class,
        SuggestionsDataLocalModule::class,
        SuggestionsDomainModule::class,

        VotingDataModule::class,
        VotingDomainModule::class,

        WatchlistDataModule::class,
        WatchlistDataLocalModule::class,
        WatchlistDataRemoteModule::class,
        WatchlistDomainModule::class
    ]
)
class CineScoutModule {

    @Single
    @DatabaseWriteDispatcher
    @OptIn(DelicateCoroutinesApi::class)
    fun databaseWriteDispatcher() = newSingleThreadContext("Database write")

    @Single
    @IoDispatcher
    fun ioDispatcher() = Dispatchers.IO
}

@Factory internal class Empty

@cinescout.AppVersionQualifier
annotation class AppVersionQualifier

