package domain

import entities.entitiesModule
import org.koin.dsl.module

val domainModule = module {
    factory { AddMovieToWatchlist(stats = get()) }
    factory { DiscoverMovies(movies = get()) }
    factory { FindMovie(movies = get()) }
    factory { GenerateDiscoverParams() }
    factory {
        GetSuggestedMovies(
            discover = get(),
            generateDiscoverParams = get(),
            getSuggestionsData = get(),
            stats = get()
        )
    }
    factory { GetSuggestionData(stats = get()) }
    factory { RateMovie(stats = get()) }
    factory { SearchMovies(movies = get()) }

} + entitiesModule
