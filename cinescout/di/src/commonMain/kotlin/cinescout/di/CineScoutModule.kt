package cinescout.di

import cinescout.movies.data.MoviesDataModule
import cinescout.movies.domain.MoviesDomainModule
import cinescout.network.NetworkModule
import cinescout.network.tmdb.TmdbNetworkModule
import cinescout.network.trakt.TraktNetworkModule
import cinescout.suggestions.domain.SuggestionsDomainModule
import org.koin.dsl.module

val CineScoutModule = module {
    includes(MoviesDataModule, MoviesDomainModule)
    includes(NetworkModule, TmdbNetworkModule, TraktNetworkModule)
    includes(SuggestionsDomainModule)
}
