package cinescout.movies.data.local

import cinescout.movies.data.LocalMovieDataSource
import cinescout.movies.data.local.mapper.DatabaseMovieMapper
import cinescout.utils.kotlin.DispatcherQualifier
import org.koin.dsl.module

val MoviesDataLocalModule = module {

    factory { DatabaseMovieMapper() }
    factory<LocalMovieDataSource> {
        RealLocalMovieDataSource(
            databaseMovieMapper = get(),
            dispatcher = get(DispatcherQualifier.Io),
            movieQueries = get(),
            movieRatingQueries = get(),
            watchlistQueries = get()
        )
    }
}
