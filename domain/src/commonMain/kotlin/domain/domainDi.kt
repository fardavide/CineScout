package domain

import domain.auth.GetTmdbAccessToken
import domain.auth.GetTmdbSessionId
import domain.auth.GetTmdbV3accountId
import domain.auth.GetTmdbV4accountId
import domain.auth.GetTraktAccessToken
import domain.auth.IsTmdbLoggedIn
import domain.auth.IsTraktLoggedIn
import domain.auth.LinkToTmdb
import domain.auth.LinkToTrakt
import domain.auth.StoreTmdbAccountId
import domain.auth.StoreTmdbCredentials
import domain.auth.StoreTraktAccessToken
import domain.profile.GetPersonalTmdbProfile
import domain.profile.GetPersonalTraktProfile
import domain.stats.AddMovieToWatchlist
import domain.stats.GenerateDiscoverParams
import domain.stats.GetMovieRating
import domain.stats.GetMoviesInWatchlist
import domain.stats.GetSuggestedMovies
import domain.stats.GetSuggestionData
import domain.stats.IsMovieInWatchlist
import domain.stats.RateMovie
import domain.stats.RemoveMovieFromWatchlist
import domain.stats.SyncTmdbRatings
import domain.stats.SyncTmdbStats
import domain.stats.SyncTmdbWatchlist
import domain.stats.SyncTraktRatings
import domain.stats.SyncTraktStats
import domain.stats.SyncTraktWatchlist
import entities.entitiesModule
import org.koin.dsl.module

val domainModule = module {

    // Auth
    factory { GetTmdbAccessToken(credentials = get()) }
    factory { GetTmdbV3accountId(credentials = get()) }
    factory { GetTmdbV4accountId(credentials = get()) }
    factory { GetTmdbSessionId(credentials = get()) }
    factory { GetTraktAccessToken(credentials = get()) }
    factory { IsTmdbLoggedIn(credentials = get()) }
    factory { IsTraktLoggedIn(credentials = get()) }
    factory { LinkToTmdb(auth = get(), syncStats = get()) }
    factory { LinkToTrakt(auth = get(), syncStats = get()) }
    factory { StoreTmdbAccountId(credentials = get()) }
    factory { StoreTmdbCredentials(credentials = get()) }
    factory { StoreTraktAccessToken(credentials = get()) }

    // Profile
    factory { GetPersonalTmdbProfile(profile = get()) }
    factory { GetPersonalTraktProfile(profile = get()) }

    // Stats
    factory { AddMovieToWatchlist(stats = get()) }
    factory { GenerateDiscoverParams() }
    factory { GetMovieRating(stats = get()) }
    factory { GetMoviesInWatchlist(stats = get()) }
    factory {
        GetSuggestedMovies(
            discover = get(),
            generateDiscoverParams = get(),
            getSuggestionsData = get(),
            stats = get()
        )
    }
    factory { GetSuggestionData(stats = get()) }
    factory { IsMovieInWatchlist(stats = get()) }
    factory { RateMovie(stats = get()) }
    factory { RemoveMovieFromWatchlist(stats = get()) }

    // Sync
    factory { SyncTmdbRatings() }
    factory { SyncTmdbStats() }
    factory { SyncTmdbWatchlist() }
    factory { SyncTraktRatings() }
    factory { SyncTraktStats() }
    factory { SyncTraktWatchlist() }

    factory { DiscoverMovies(movies = get()) }
    factory { FindMovie(movies = get()) }
    factory { SearchMovies(movies = get()) }

} + entitiesModule
