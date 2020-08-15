package domain

import org.koin.dsl.module

val domainModule = module {
    factory { DiscoverMovies(movies = get()) }
    factory { FindMovie(movies = get()) }
    factory { GetSuggestedMovies(discover = get(), getSuggestionsData = get(), stats = get()) }
    factory { GetSuggestionData(stats = get()) }
    factory { RateMovie(stats = get()) }
    factory { SearchMovies(movies = get()) }
}
