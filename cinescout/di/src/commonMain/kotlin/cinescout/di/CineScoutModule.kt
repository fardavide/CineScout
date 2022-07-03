package cinescout.di

import cinescout.movies.domain.MoviesDomainModule
import cinescout.network.NetworkModule
import cinescout.network.trakt.TraktNetworkModule
import cinescout.stats.domain.StatsDomainModule
import cinescout.suggestions.domain.SuggestionsDomainModule
import org.koin.dsl.module

val CineScoutModule = module {
    includes(MoviesDomainModule)
    includes(NetworkModule, TraktNetworkModule, TraktNetworkModule)
    includes(StatsDomainModule)
    includes(SuggestionsDomainModule)
}
