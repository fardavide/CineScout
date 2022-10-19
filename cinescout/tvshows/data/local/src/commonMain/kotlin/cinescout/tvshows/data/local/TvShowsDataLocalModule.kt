package cinescout.tvshows.data.local

import cinescout.tvshows.data.LocalTvShowDataSource
import cinescout.tvshows.data.local.mapper.DatabaseTvShowCreditsMapper
import cinescout.tvshows.data.local.mapper.DatabaseTvShowMapper
import cinescout.tvshows.data.local.mapper.DatabaseTvShowVideoMapper
import cinescout.utils.kotlin.DispatcherQualifier
import org.koin.dsl.module

val TvShowsDataLocalModule = module {

    factory { DatabaseTvShowCreditsMapper() }
    factory { DatabaseTvShowMapper() }
    factory { DatabaseTvShowVideoMapper() }
    factory<LocalTvShowDataSource> {
        RealLocalTvShowDataSource(
            databaseTvShowCreditsMapper = get(),
            databaseTvShowMapper = get(),
            databaseTvShowVideoMapper = get(),
            genreQueries = get(),
            keywordQueries = get(),
            likedTvShowQueries = get(),
            personQueries = get(),
            readDispatcher = get(DispatcherQualifier.Io),
            transacter = get(),
            tvShowBackdropQueries = get(),
            tvShowCastMemberQueries = get(),
            tvShowCrewMemberQueries = get(),
            tvShowGenreQueries = get(),
            tvShowKeywordQueries = get(),
            tvShowPosterQueries = get(),
            tvShowQueries = get(),
            tvShowRatingQueries = get(),
            tvShowVideoQueries = get(),
            watchlistQueries = get(),
            writeDispatcher = get(DispatcherQualifier.DatabaseWrite)
        )
    }
}
