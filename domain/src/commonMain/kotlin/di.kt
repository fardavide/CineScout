import org.koin.dsl.module

val domainModule = module {
    factory { DiscoverMovies(movies = get()) }
    factory { GetSuggestedMovies(discover = get(), getSuggestionsData = get(), start = get()) }
    factory { GetSuggestionData(stats = get()) }
    factory { RateMovie(stats = get()) }
    factory { SearchMovies(movies = get()) }
}
