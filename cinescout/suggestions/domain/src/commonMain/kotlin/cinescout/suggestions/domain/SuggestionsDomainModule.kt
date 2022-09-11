package cinescout.suggestions.domain

import cinescout.suggestions.domain.usecase.GenerateSuggestedMovies
import cinescout.suggestions.domain.usecase.GetSuggestedMovies
import cinescout.suggestions.domain.usecase.GetSuggestedMoviesWithExtras
import cinescout.suggestions.domain.usecase.IsLoggedIn
import cinescout.suggestions.domain.usecase.UpdateSuggestedMovies
import org.koin.dsl.module

val SuggestionsDomainModule = module {

    factory {
        GenerateSuggestedMovies(
            getAllDislikedMovies = get(),
            getAllLikedMovies = get(),
            getAllRatedMovies = get(),
            getAllWatchlistMovies = get(),
            movieRepository = get()
        )
    }
    factory { GetSuggestedMovies(movieRepository = get(), updateSuggestedMovies = get()) }
    factory { GetSuggestedMoviesWithExtras(getSuggestedMovies = get(), getMovieExtras = get()) }
    factory { IsLoggedIn(isTmdbLinked = get(), isTraktLinked = get()) }
    factory { UpdateSuggestedMovies(generateSuggestedMovies = get(), movieRepository = get()) }
}
