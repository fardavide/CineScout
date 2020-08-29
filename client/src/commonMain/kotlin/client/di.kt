package client

import client.viewModel.GetSuggestedMovieViewModel
import client.viewModel.RateMovieViewModel
import client.viewModel.SearchViewModel
import domain.domainModule
import kotlinx.coroutines.CoroutineScope
import movies.remote.tmdb.tmdbRemoteMoviesModule
import org.koin.dsl.module
import stats.local.localStatsModule

val clientModule = module {

    single<Navigator> { NavigatorImpl() }

    factory { (scope: CoroutineScope) ->
        GetSuggestedMovieViewModel(
            scope = scope,
            dispatchers = get(),
            getSuggestedMovies = get(),
            rateMovie = get(),
        )
    }
    factory { (scope: CoroutineScope) ->
        RateMovieViewModel(
            scope = scope,
            dispatchers = get(),
            rateMovie = get(),
            findMovie = get()
        )
    }
    factory { (scope: CoroutineScope) ->
        SearchViewModel(
            scope = scope,
            dispatchers = get(),
            searchMovies = get(),
        )
    }

} + domainModule + localStatsModule + tmdbRemoteMoviesModule
