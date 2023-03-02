package cinescout.di.kotlin

import cinescout.KotlinUtilsModule
import cinescout.account.domain.AccountDomainModule
import cinescout.account.tmdb.data.AccountTmdbDataModule
import cinescout.account.tmdb.data.local.AccountTmdbDataLocalModule
import cinescout.account.tmdb.data.remote.AccountTmdbDataRemoteModule
import cinescout.account.tmdb.domain.AccountTmdbDomainModule
import cinescout.account.trakt.data.AccountTraktDataModule
import cinescout.account.trakt.data.local.AccountTraktDataLocalModule
import cinescout.account.trakt.data.remote.AccountTraktDataRemoteModule
import cinescout.account.trakt.domain.AccountTraktDomainModule
import cinescout.auth.domain.AuthDomainModule
import cinescout.auth.tmdb.data.AuthTmdbDataModule
import cinescout.auth.tmdb.data.local.AuthTmdbDataLocalModule
import cinescout.auth.tmdb.data.remote.AuthTmdbDataRemoteModule
import cinescout.auth.tmdb.domain.AuthTmdbDomainModule
import cinescout.auth.trakt.data.AuthTraktDataModule
import cinescout.auth.trakt.data.local.AuthTraktDataLocalModule
import cinescout.auth.trakt.data.remote.AuthTraktDataRemoteModule
import cinescout.auth.trakt.domain.AuthTraktDomainModule
import cinescout.database.DatabaseModule
import cinescout.movies.data.MoviesDataModule
import cinescout.movies.data.local.MoviesDataLocalModule
import cinescout.movies.data.remote.MoviesDataRemoteModule
import cinescout.movies.data.remote.tmdb.MoviesDataRemoteTmdbModule
import cinescout.movies.data.remote.trakt.MoviesDataRemoteTraktModule
import cinescout.movies.domain.MoviesDomainModule
import cinescout.network.NetworkModule
import cinescout.network.tmdb.NetworkTmdbModule
import cinescout.network.trakt.NetworkTraktModule
import cinescout.search.domain.SearchDomainModule
import cinescout.settings.data.SettingsDataModule
import cinescout.settings.data.local.SettingsDataLocalModule
import cinescout.settings.domain.SettingsDomainModule
import cinescout.store.StoreModule
import cinescout.suggestions.domain.SuggestionsDomainModule
import cinescout.tvshows.data.TvShowsDataModule
import cinescout.tvshows.data.local.TvShowsDataLocalModule
import cinescout.tvshows.data.remote.TvShowsDataRemoteModule
import cinescout.tvshows.data.remote.tmdb.TvShowsDataRemoteTmdbModule
import cinescout.tvshows.data.remote.trakt.TvShowsDataRemoteTraktModule
import cinescout.tvshows.domain.TvShowsDomainModule
import cinescout.utils.kotlin.DispatcherQualifier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ksp.generated.module

val CineScoutModule = module {
    includes(
        AccountDomainModule().module,
        AccountTmdbDataModule().module,
        AccountTmdbDataLocalModule().module,
        AccountTmdbDataRemoteModule().module,
        AccountTmdbDomainModule().module,

        AccountTraktDataModule().module,
        AccountTraktDataLocalModule().module,
        AccountTraktDataRemoteModule().module,
        AccountTraktDomainModule().module,

        AuthDomainModule().module,

        AuthTmdbDataModule().module,
        AuthTmdbDataLocalModule().module,
        AuthTmdbDataRemoteModule().module,
        AuthTmdbDomainModule().module,

        AuthTraktDataModule().module,
        AuthTraktDataLocalModule().module,
        AuthTraktDataRemoteModule().module,
        AuthTraktDomainModule().module,

        DatabaseModule().module,

        KotlinUtilsModule().module,

        MoviesDataLocalModule().module,
        MoviesDataModule().module,
        MoviesDataRemoteModule().module,
        MoviesDataRemoteTmdbModule().module,
        MoviesDataRemoteTraktModule().module,
        MoviesDomainModule().module,

        NetworkModule().module,
        NetworkTmdbModule().module,
        NetworkTraktModule().module,

        SearchDomainModule().module,

        SettingsDataModule().module,
        SettingsDataLocalModule().module,
        SettingsDomainModule().module,

        StoreModule().module,

        SuggestionsDataModule().module,
        SuggestionsDomainModule().module,

        TvShowsDataLocalModule().module,
        TvShowsDataModule().module,
        TvShowsDataRemoteModule().module,
        TvShowsDataRemoteTmdbModule().module,
        TvShowsDataRemoteTraktModule().module,
        TvShowsDomainModule().module
    )

    factory(named(DispatcherQualifier.Io)) { Dispatchers.IO }
    single<CoroutineDispatcher>(named(DispatcherQualifier.DatabaseWrite)) {
        @OptIn(DelicateCoroutinesApi::class)
        newSingleThreadContext("Database write")
    }
}

val AppVersionQualifier = named(cinescout.AppVersionQualifier)
