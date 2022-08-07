package cinescout.suggestions.domain

import cinescout.suggestions.domain.usecase.BuildDiscoverMoviesParams
import cinescout.suggestions.domain.usecase.GetSuggestedMovies
import org.koin.dsl.module

val SuggestionsDomainModule = module {

    factory { BuildDiscoverMoviesParams() }
    factory {
        GetSuggestedMovies(
            buildDiscoverMoviesParams = get(),
            getAllDislikedMovies = get(),
            getAllLikedMovies = get(),
            getAllRatedMovies = get(),
            movieRepository = get()
        )
    }
}
