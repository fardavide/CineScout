package cinescout.database

import org.koin.core.scope.Scope
import org.koin.dsl.module

val DatabaseQueriesModule = module {

    factory { database.appSettingsQueries }
    factory { database.likedMovieQueries }
    factory { database.likedTvShowQueries }
    factory { database.genreQueries }
    factory { database.keywordQueries }
    factory { database.movieBackdropQueries }
    factory { database.movieCastMemberQueries }
    factory { database.movieCrewMemberQueries }
    factory { database.movieGenreQueries }
    factory { database.movieKeywordQueries }
    factory { database.movieQueries }
    factory { database.moviePosterQueries }
    factory { database.movieRatingQueries }
    factory { database.movieRecommendationQueries }
    factory { database.movieVideoQueries }
    factory { database.personQueries }
    factory { database.storeFetchDataQueries }
    factory { database.suggestedMovieQueries }
    factory { database.tmdbAccountQueries }
    factory { database.tmdbAuthStateQueries }
    factory { database.traktAccountQueries }
    factory { database.traktAuthStateQueries }
    factory { database.tvShowBackdropQueries }
    factory { database.tvShowCastMemberQueries }
    factory { database.tvShowCrewMemberQueries }
    factory { database.tvShowGenreQueries }
    factory { database.tvShowKeywordQueries }
    factory { database.tvShowPosterQueries }
    factory { database.tvShowQueries }
    factory { database.tvShowRatingQueries }
    factory { database.tvShowVideoQueries }
    factory { database.tvShowWatchlistQueries }
    factory { database.watchlistQueries }
}

private val Scope.database get() = get<Database>()

