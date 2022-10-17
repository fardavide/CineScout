package cinescout.tvshows.domain

import cinescout.tvshows.domain.usecase.*
import org.koin.dsl.module

val TvShowsDomainModule = module {

    factory { AddTvShowToWatchlist(tvShowRepository = get()) }
    factory { GetAllRatedTvShows(tvShowRepository = get()) }
    factory { GetAllWatchlistTvShows(tvShowRepository = get()) }
    factory { GetIsTvShowInWatchlist(getAllWatchlistTvShows = get()) }
    factory { GetTvShowCredits(tvShowRepository = get()) }
    factory { GetTvShowDetails(tvShowRepository = get()) }
    factory {
        GetTvShowExtras(
            getIsTvShowInWatchlist = get(),
            getTvShowCredits = get(),
            getTvShowDetails = get(),
            getTvShowKeywords = get(),
            getTvShowPersonalRating = get()
        )
    }
    factory { GetTvShowKeywords(tvShowRepository = get()) }
    factory { GetTvShowMedia(tvShowRepository = get()) }
    factory { GetTvShowPersonalRating(getAllRatedTvShows = get()) }
    factory { RateTvShow(tvShowRepository = get()) }
    factory { RemoveTvShowFromWatchlist(tvShowRepository = get()) }
}
