package cinescout.tvshows.domain

import cinescout.tvshows.domain.usecase.GetAllWatchlistTvShows
import org.koin.dsl.module

val TvShowsDomainModule = module {

    factory { GetAllWatchlistTvShows(tvShowRepository = get()) }
}
