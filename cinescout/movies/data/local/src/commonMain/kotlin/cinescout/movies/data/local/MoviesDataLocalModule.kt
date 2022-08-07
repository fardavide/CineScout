package cinescout.movies.data.local

import cinescout.movies.data.LocalMovieDataSource
import cinescout.movies.data.local.mapper.DatabaseMovieCreditsMapper
import cinescout.movies.data.local.mapper.DatabaseMovieMapper
import cinescout.utils.kotlin.DispatcherQualifier
import org.koin.dsl.module

val MoviesDataLocalModule = module {

    factory { DatabaseMovieCreditsMapper() }
    factory { DatabaseMovieMapper() }
    factory<LocalMovieDataSource> {
        RealLocalMovieDataSource(
            databaseMovieCreditsMapper = get(),
            databaseMovieMapper = get(),
            dispatcher = get(DispatcherQualifier.Io),
            likedMovieQueries = get(),
            movieCastMemberQueries = get(),
            movieCrewMemberQueries = get(),
            movieQueries = get(),
            movieRatingQueries = get(),
            personQueries = get(),
            watchlistQueries = get()
        )
    }
}
