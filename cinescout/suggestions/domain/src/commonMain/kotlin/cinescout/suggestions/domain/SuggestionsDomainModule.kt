package cinescout.suggestions.domain

import cinescout.suggestions.domain.usecase.BuildDiscoverMoviesParams
import cinescout.suggestions.domain.usecase.GenerateSuggestedMovies
import cinescout.suggestions.domain.usecase.UpdateSuggestedMovies
import org.koin.dsl.module

val SuggestionsDomainModule = module {

    factory { BuildDiscoverMoviesParams() }
    factory {
        GenerateSuggestedMovies(
            buildDiscoverMoviesParams = get(),
            getAllDislikedMovies = get(),
            getAllLikedMovies = get(),
            getAllRatedMovies = get(),
            getMovieExtras = get(),
            movieRepository = get()
        )
    }
    factory {
        UpdateSuggestedMovies(
            generateSuggestedMovies = get(),
            movieRepository = get()
        )
    }
}
