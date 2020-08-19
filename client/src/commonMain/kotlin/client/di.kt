package client

import client.viewModel.GetSuggestedMovieViewModel
import client.viewModel.RateMovieViewModel
import client.viewModel.SearchViewModel
import domain.domainModule
import movies.remote.tmdb.tmdbRemoteMoviesModule
import org.koin.dsl.module
import stats.local.localStatsModule

val clientModule = module {

    factory {
        GetSuggestedMovieViewModel(
            scope = get(),
            dispatchers = get(),
            getSuggestedMovies = get(),
            rateMovie = get(),
        )
    }
    factory {
        RateMovieViewModel(
            scope = get(),
            dispatchers = get(),
            rateMovie = get(),
            findMovie = get()
        )
    }
    factory {
        SearchViewModel(
            scope = get(),
            dispatchers = get(),
            searchMovies = get(),
        )
    }

} + domainModule + localStatsModule + tmdbRemoteMoviesModule
