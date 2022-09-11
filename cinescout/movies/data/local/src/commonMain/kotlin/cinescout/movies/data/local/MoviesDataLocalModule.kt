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
            transacter = get(),
            databaseMovieCreditsMapper = get(),
            databaseMovieMapper = get(),
            readDispatcher = get(DispatcherQualifier.Io),
            genreQueries = get(),
            keywordQueries = get(),
            likedMovieQueries = get(),
            movieCastMemberQueries = get(),
            movieCrewMemberQueries = get(),
            movieGenreQueries = get(),
            movieKeywordQueries = get(),
            movieQueries = get(),
            movieRatingQueries = get(),
            movieRecommendationQueries = get(),
            personQueries = get(),
            suggestedMovieQueries = get(),
            watchlistQueries = get(),
            writeDispatcher = get(DispatcherQualifier.DatabaseWrite)
        )
    }
}
