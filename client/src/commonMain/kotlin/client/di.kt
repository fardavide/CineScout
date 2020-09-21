package client

import client.viewModel.GetSuggestedMovieViewModel
import client.viewModel.MovieDetailsViewModel
import client.viewModel.RateMovieViewModel
import client.viewModel.SearchViewModel
import domain.domainModule
import entities.TmdbId
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
            addMovieToWatchlist = get(),
            rateMovie = get(),
        )
    }
    factory { (scope: CoroutineScope, id: TmdbId) ->
        MovieDetailsViewModel(
            scope = scope,
            dispatchers = get(),
            movieId = id,
            findMovie = get(),
            getMovieRating = get(),
            isMovieInWatchlist = get(),
            addMovieToWatchlist = get(),
            removeMovieFromWatchlist = get(),
            rateMovie = get(),
        )
    }
    factory { (scope: CoroutineScope) ->
        RateMovieViewModel(
            scope = scope,
            dispatchers = get(),
            addMovieToWatchlist = get(),
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
