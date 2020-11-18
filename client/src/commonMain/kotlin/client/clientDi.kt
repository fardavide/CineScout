package client

import auth.tmdb.tmdbAuthModule
import client.viewModel.GetSuggestedMovieViewModel
import client.viewModel.MovieDetailsViewModel
import client.viewModel.RateMovieViewModel
import client.viewModel.SearchViewModel
import client.viewModel.WatchlistViewModel
import domain.domainModule
import entities.TmdbId
import kotlinx.coroutines.CoroutineScope
import movies.remote.tmdb.tmdbRemoteMoviesModule
import org.koin.dsl.module
import profile.tmdb.local.localTmdbProfileModule
import profile.tmdb.remote.remoteTmdbProfileModule
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
    factory { (scope: CoroutineScope) ->
        WatchlistViewModel(
            scope = scope,
            dispatchers = get(),
            getMoviesInWatchlist = get()
        )
    }

} +
    // domain
    domainModule +

    // auth
    tmdbAuthModule +

    // movies
    tmdbRemoteMoviesModule +

    // profile
    localTmdbProfileModule +
    remoteTmdbProfileModule +

    // stats
    localStatsModule
