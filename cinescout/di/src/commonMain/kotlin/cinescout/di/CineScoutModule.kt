package cinescout.di

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
import cinescout.suggestions.domain.SuggestionsDomainModule
import org.koin.core.module.Module
import org.koin.dsl.module

val CineScoutModule = module {
    includes(DatabaseModule)
    includes(DispatcherModule)
    includes(
        MoviesDataModule,
        MoviesDataLocalModule,
        MoviesDataRemoteModule,
        MoviesDataRemoteTmdbModule,
        MoviesDataRemoteTraktModule,
        MoviesDomainModule
    )
    includes(NetworkModule, NetworkTmdbModule, NetworkTraktModule)
    includes(SuggestionsDomainModule)
}

expect val DispatcherModule: Module
