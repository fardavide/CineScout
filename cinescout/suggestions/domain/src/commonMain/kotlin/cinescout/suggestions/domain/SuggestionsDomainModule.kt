package cinescout.suggestions.domain

import cinescout.suggestions.domain.usecase.GenerateSuggestedMovies
import cinescout.suggestions.domain.usecase.GenerateSuggestedTvShows
import cinescout.suggestions.domain.usecase.GetSuggestedMovies
import cinescout.suggestions.domain.usecase.GetSuggestedMoviesWithExtras
import cinescout.suggestions.domain.usecase.GetSuggestedTvShows
import cinescout.suggestions.domain.usecase.GetSuggestedTvShowsWithExtras
import cinescout.suggestions.domain.usecase.IsLoggedIn
import cinescout.suggestions.domain.usecase.UpdateSuggestedMovies
import cinescout.suggestions.domain.usecase.UpdateSuggestedTvShows
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
    factory {
        GenerateSuggestedTvShows(
            getAllDislikedTvShows = get(),
            getAllLikedTvShows = get(),
            getAllRatedTvShows = get(),
            getAllWatchlistTvShows = get(),
            tvShowRepository = get()
        )
    }
    factory { GetSuggestedMovies(movieRepository = get(), updateSuggestedMovies = get()) }
    factory { GetSuggestedMoviesWithExtras(getSuggestedMovies = get(), getMovieExtras = get()) }
    factory { GetSuggestedTvShows(tvShowRepository = get(), updateSuggestedTvShows = get()) }
    factory { GetSuggestedTvShowsWithExtras(getSuggestedTvShows = get(), getTvShowExtras = get()) }
    factory { IsLoggedIn(isTmdbLinked = get(), isTraktLinked = get()) }
    factory { UpdateSuggestedMovies(generateSuggestedMovies = get(), movieRepository = get()) }
    factory { UpdateSuggestedTvShows(generateSuggestedTvShows = get(), tvShowRepository = get()) }
}
