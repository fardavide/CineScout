package client

import domain.domainModule
import movies.remote.tmdb.tmdbRemoteMoviesModule
import stats.local.localStatsModule

val clientModule = domainModule + localStatsModule + tmdbRemoteMoviesModule
