package cinescout.di.kotlin

import cinescout.KotlinUtilsModule
import cinescout.account.domain.AccountDomainModule
import cinescout.account.trakt.data.AccountTraktDataModule
import cinescout.account.trakt.data.local.AccountTraktDataLocalModule
import cinescout.account.trakt.data.remote.AccountTraktDataRemoteModule
import cinescout.anticipated.data.AnticipatedDataModule
import cinescout.anticipated.data.local.AnticipatedDataLocalModule
import cinescout.anticipated.data.remote.AnticipatedDataRemoteModule
import cinescout.anticipated.domain.AnticipatedDomainModule
import cinescout.auth.domain.AuthDomainModule
import cinescout.auth.trakt.data.AuthDataModule
import cinescout.auth.trakt.data.local.AuthDataLocalModule
import cinescout.auth.trakt.data.remote.AuthDataRemoteModule
import cinescout.database.DatabaseModule
import cinescout.details.domain.DetailsDomainModule
import cinescout.fetchdata.data.FetchDataDataModule
import cinescout.history.data.HistoryDataModule
import cinescout.history.data.local.HistoryDataLocalModule
import cinescout.history.data.remote.HistoryDataRemoteModule
import cinescout.history.domain.HistoryDomainModule
import cinescout.lists.data.local.ListsDataLocalModule
import cinescout.lists.data.remote.ListsDataRemoteModule
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
import cinescout.perfomance.PerformanceModule
import cinescout.popular.data.PopularDataModule
import cinescout.popular.data.local.PopularDataLocalModule
import cinescout.popular.data.remote.PopularDataRemoteModule
import cinescout.popular.domain.PopularDomainModule
import cinescout.progress.data.ProgressDataModule
import cinescout.progress.data.local.ProgressDataLocalModule
import cinescout.progress.domain.ProgressDomainModule
import cinescout.rating.data.RatingDataModule
import cinescout.rating.data.local.RatingDataLocalModule
import cinescout.rating.data.remote.RatingDataRemoteModule
import cinescout.rating.domain.RatingDomainModule
import cinescout.recommended.data.RecommendedDataModule
import cinescout.recommended.data.local.RecommendedDataLocalModule
import cinescout.recommended.data.remote.RecommendedDataRemoteModule
import cinescout.recommended.domain.RecommendedDomainModule
import cinescout.screenplay.data.ScreenplayDataModule
import cinescout.screenplay.data.local.ScreenplayDataLocalModule
import cinescout.screenplay.data.remote.ScreenplayDataRemoteModule
import cinescout.screenplay.data.remote.tmdb.ScreenplayDataRemoteTmdbModule
import cinescout.screenplay.domain.ScreenplayDomainModule
import cinescout.search.data.SearchDataModule
import cinescout.search.data.local.SearchDataLocalModule
import cinescout.search.data.remote.SearchDataRemoteModule
import cinescout.search.domain.SearchDomainModule
import cinescout.seasons.data.SeasonsDataModule
import cinescout.seasons.data.local.SeasonsDataLocalModule
import cinescout.seasons.data.remote.SeasonsDataRemoteModule
import cinescout.seasons.domain.SeasonsDomainModule
import cinescout.settings.data.SettingsDataModule
import cinescout.settings.data.local.SettingsDataLocalModule
import cinescout.settings.domain.SettingsDomainModule
import cinescout.suggestions.data.SuggestionsDataModule
import cinescout.suggestions.data.local.SuggestionsDataLocalModule
import cinescout.suggestions.domain.SuggestionsDomainModule
import cinescout.sync.data.SyncDataModule
import cinescout.sync.data.local.SyncDataLocalModule
import cinescout.sync.data.remote.SyncDataRemoteModule
import cinescout.sync.domain.SyncDomainModule
import cinescout.trending.data.TrendingDataModule
import cinescout.trending.data.local.TrendingDataLocalModule
import cinescout.trending.data.remote.TrendingDataRemoteModule
import cinescout.trending.domain.TrendingDomainModule
import cinescout.utils.kotlin.ComputationDispatcher
import cinescout.utils.kotlin.DatabaseWriteDispatcher
import cinescout.utils.kotlin.IoDispatcher
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
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import screenplay.data.remote.trakt.ScreenplayDataRemoteTraktModule

@ComponentScan
@Module(
    includes = [
        AccountDomainModule::class,
        AccountTraktDataModule::class,
        AccountTraktDataLocalModule::class,
        AccountTraktDataRemoteModule::class,

        AnticipatedDataModule::class,
        AnticipatedDataLocalModule::class,
        AnticipatedDataRemoteModule::class,
        AnticipatedDomainModule::class,

        AuthDataModule::class,
        AuthDataLocalModule::class,
        AuthDataRemoteModule::class,
        AuthDomainModule::class,

        DatabaseModule::class,

        DetailsDomainModule::class,

        FetchDataDataModule::class,

        HistoryDataModule::class,
        HistoryDataLocalModule::class,
        HistoryDataRemoteModule::class,
        HistoryDomainModule::class,

        KotlinUtilsModule::class,

        ListsDataLocalModule::class,
        ListsDataRemoteModule::class,

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

        PerformanceModule::class,

        PopularDataModule::class,
        PopularDataLocalModule::class,
        PopularDataRemoteModule::class,
        PopularDomainModule::class,

        ProgressDataModule::class,
        ProgressDataLocalModule::class,
        ProgressDomainModule::class,

        RatingDataModule::class,
        RatingDataLocalModule::class,
        RatingDataRemoteModule::class,
        RatingDomainModule::class,

        RecommendedDataModule::class,
        RecommendedDataLocalModule::class,
        RecommendedDataRemoteModule::class,
        RecommendedDomainModule::class,

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

        SeasonsDataModule::class,
        SeasonsDataLocalModule::class,
        SeasonsDataRemoteModule::class,
        SeasonsDomainModule::class,

        SettingsDataModule::class,
        SettingsDataLocalModule::class,
        SettingsDomainModule::class,

        SuggestionsDataModule::class,
        SuggestionsDataLocalModule::class,
        SuggestionsDomainModule::class,

        SyncDataModule::class,
        SyncDataLocalModule::class,
        SyncDataRemoteModule::class,
        SyncDomainModule::class,

        TrendingDataModule::class,
        TrendingDataLocalModule::class,
        TrendingDataRemoteModule::class,
        TrendingDomainModule::class,

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
    @Named(ComputationDispatcher)
    fun computationDispatcher() = Dispatchers.Default

    @Single
    @Named(DatabaseWriteDispatcher)
    @OptIn(DelicateCoroutinesApi::class)
    fun databaseWriteDispatcher(): CoroutineDispatcher = newSingleThreadContext("Database write")

    @Single
    @Named(IoDispatcher)
    fun ioDispatcher() = Dispatchers.IO
}

const val AppVersion = cinescout.AppVersion
