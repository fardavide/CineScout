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
import cinescout.movies.data.MoviesDataModule
import cinescout.movies.data.local.MoviesDataLocalModule
import cinescout.movies.data.remote.MoviesDataRemoteModule
import cinescout.movies.data.remote.tmdb.MoviesDataRemoteTmdbModule
import cinescout.movies.data.remote.trakt.MoviesDataRemoteTraktModule
import cinescout.movies.domain.MoviesDomainModule
import cinescout.network.NetworkModule
import cinescout.network.tmdb.NetworkTmdbModule
import cinescout.network.trakt.NetworkTraktModule
import cinescout.screenplay.data.ScreenplayDataModule
import cinescout.screenplay.data.local.ScreenplayDataLocalModule
import cinescout.screenplay.data.remote.ScreenplayDataRemoteModule
import cinescout.screenplay.domain.ScreenplayDomainModule
import cinescout.search.domain.SearchDomainModule
import cinescout.settings.data.SettingsDataModule
import cinescout.settings.data.local.SettingsDataLocalModule
import cinescout.settings.domain.SettingsDomainModule
import cinescout.store.StoreModule
import cinescout.suggestions.data.SuggestionsDataModule
import cinescout.suggestions.data.local.SuggestionsDataLocalModule
import cinescout.suggestions.domain.SuggestionsDomainModule
import cinescout.tvshows.data.TvShowsDataModule
import cinescout.tvshows.data.local.TvShowsDataLocalModule
import cinescout.tvshows.domain.TvShowsDomainModule
import cinescout.utils.kotlin.DispatcherQualifier
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

        ScreenplayDataModule().module,
        ScreenplayDataLocalModule().module,
        ScreenplayDataRemoteModule().module,
        ScreenplayDataRemoteTraktModule().module,
        ScreenplayDomainModule().module,

        SearchDomainModule().module,

        SettingsDataModule().module,
        SettingsDataLocalModule().module,
        SettingsDomainModule().module,

        StoreModule().module,

        SuggestionsDataModule().module,
        SuggestionsDataLocalModule().module,
        SuggestionsDomainModule().module,

        TvShowsDataLocalModule().module,
        TvShowsDataModule().module,
        TvShowsDomainModule().module
    )

    factory(named(DispatcherQualifier.Io)) { Dispatchers.IO }
    single<CoroutineDispatcher>(named(DispatcherQualifier.DatabaseWrite)) {
        @OptIn(DelicateCoroutinesApi::class)
        newSingleThreadContext("Database write")
    }
}

val AppVersionQualifier = named(cinescout.AppVersionQualifier)
