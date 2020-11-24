package client

import auth.tmdb.testTmdbAuthModule
import auth.tmdb.tmdbAuthModule
import auth.trakt.traktAuthModule
import client.viewModel.DrawerViewModel
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
import profile.trakt.local.localTraktProfileModule
import profile.trakt.remote.remoteTraktProfileModule
import stats.local.localStatsModule
import stats.remote.tmdb.tmdbRemoteStatsModule
import stats.remote.trakt.traktRemoteStatsModule

val clientModule = module {

    single<Navigator> { NavigatorImpl() }

    single { (scope: CoroutineScope) ->
        DrawerViewModel(
            scope = scope,
            getPersonalTmdbProfile = get(),
            getPersonalTraktProfile = get(),
            isTmdbLoggedIn = get(),
            isTraktLoggedIn = get(),
            linkToTmdb = get(),
            linkToTrakt = get()
        )
    }
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
            addMovieToWatchlist = get(),
            rateMovie = get(),
            findMovie = get()
        )
    }
    factory { (scope: CoroutineScope) ->
        SearchViewModel(
            scope = scope,
            searchMovies = get(),
        )
    }
    factory { (scope: CoroutineScope) ->
        WatchlistViewModel(
            scope = scope,
            getMoviesInWatchlist = get()
        )
    }

} +
    // domain
    domainModule +

    // auth
    tmdbAuthModule +
    traktAuthModule +

    // movies
    tmdbRemoteMoviesModule +

    // profile
    localTmdbProfileModule +
    remoteTmdbProfileModule +
    localTraktProfileModule +
    remoteTraktProfileModule +

    // stats
    localStatsModule +
    tmdbRemoteStatsModule +
    traktRemoteStatsModule

val testAuthModule = testTmdbAuthModule
