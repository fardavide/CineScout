package cinescout.tvshows.domain

import cinescout.tvshows.domain.usecase.AddTvShowToWatchlist
import cinescout.tvshows.domain.usecase.GetAllDislikedTvShows
import cinescout.tvshows.domain.usecase.GetAllLikedTvShows
import cinescout.tvshows.domain.usecase.GetAllRatedTvShows
import cinescout.tvshows.domain.usecase.GetAllWatchlistTvShows
import cinescout.tvshows.domain.usecase.GetIsTvShowInWatchlist
import cinescout.tvshows.domain.usecase.GetTvShowCredits
import cinescout.tvshows.domain.usecase.GetTvShowDetails
import cinescout.tvshows.domain.usecase.GetTvShowExtras
import cinescout.tvshows.domain.usecase.GetTvShowKeywords
import cinescout.tvshows.domain.usecase.GetTvShowMedia
import cinescout.tvshows.domain.usecase.GetTvShowPersonalRating
import cinescout.tvshows.domain.usecase.RateTvShow
import cinescout.tvshows.domain.usecase.RemoveTvShowFromWatchlist
import org.koin.dsl.module

val TvShowsDomainModule = module {

    factory { AddTvShowToWatchlist(tvShowRepository = get()) }
    factory { GetAllDislikedTvShows(tvShowRepository = get()) }
    factory { GetAllLikedTvShows(tvShowRepository = get()) }
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
