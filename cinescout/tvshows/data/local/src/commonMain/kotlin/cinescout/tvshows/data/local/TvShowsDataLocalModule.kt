package cinescout.tvshows.data.local

import cinescout.tvshows.data.LocalTvShowDataSource
import cinescout.tvshows.data.local.mapper.DatabaseTvShowMapper
import cinescout.utils.kotlin.DispatcherQualifier
import org.koin.dsl.module

val TvShowsDataLocalModule = module {

    factory { DatabaseTvShowMapper() }
    factory<LocalTvShowDataSource> {
        RealLocalTvShowDataSource(
            databaseTvShowMapper = get(),
            genreQueries = get(),
            readDispatcher = get(DispatcherQualifier.Io),
            transacter = get(),
            tvShowGenreQueries = get(),
            tvShowQueries = get(),
            watchlistQueries = get(),
            writeDispatcher = get(DispatcherQualifier.DatabaseWrite)
        )
    }
}
