package cinescout.di.kotlin

import cinescout.KotlinUtilsModule
import cinescout.account.tmdb.data.AccountTmdbDataModule
import cinescout.account.tmdb.data.remote.AccountTmdbDataRemoteModule
import cinescout.account.tmdb.domain.AccountTmdbDomainModule
import cinescout.account.trakt.data.AccountTraktDataModule
import cinescout.account.trakt.data.local.AccountTraktDataLocalModule
import cinescout.account.trakt.data.remote.AccountTraktDataRemoteModule
import cinescout.account.trakt.domain.AccountTraktDomainModule
import cinescout.accuount.tmdb.data.local.AccountTmdbDataLocalModule
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
import cinescout.store.StoreModule
import cinescout.suggestions.domain.SuggestionsDomainModule
import org.koin.core.module.Module
import org.koin.dsl.module

val CineScoutModule = module {
    includes(
        AuthTmdbDataModule,
        AuthTmdbDataLocalModule,
        AuthTmdbDataRemoteModule,
        AuthTmdbDomainModule
    )
    includes(
        AuthTraktDataModule,
        AuthTraktDataLocalModule,
        AuthTraktDataRemoteModule,
        AuthTraktDomainModule
    )
    includes(
        AccountTmdbDataModule,
        AccountTmdbDataLocalModule,
        AccountTmdbDataRemoteModule,
        AccountTmdbDomainModule
    )
    includes(
        AccountTraktDataModule,
        AccountTraktDataLocalModule,
        AccountTraktDataRemoteModule,
        AccountTraktDomainModule
    )
    includes(DatabaseModule)
    includes(DispatcherModule)
    includes(KotlinUtilsModule)
    includes(
        MoviesDataLocalModule,
        MoviesDataModule,
        MoviesDataRemoteModule,
        MoviesDataRemoteTmdbModule,
        MoviesDataRemoteTraktModule,
        MoviesDomainModule
    )
    includes(
        NetworkModule,
        NetworkTmdbModule,
        NetworkTraktModule
    )
    includes(StoreModule)
    includes(SuggestionsDomainModule)
}

expect val DispatcherModule: Module

val AppVersionQualifier = cinescout.AppVersionQualifier
