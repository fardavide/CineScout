package cinescout.database

import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class DatabaseQueriesModule {

    @Factory
    fun appSettingsQueries(database: Database) = database.appSettingsQueries

    @Factory
    fun likedMovieQueries(database: Database) = database.likedMovieQueries

    @Factory
    fun likedTvShowQueries(database: Database) = database.likedTvShowQueries

    @Factory
    fun genreQueries(database: Database) = database.genreQueries

    @Factory
    fun keywordQueries(database: Database) = database.keywordQueries

    @Factory
    fun movieBackdropQueries(database: Database) = database.movieBackdropQueries

    @Factory
    fun movieCastMemberQueries(database: Database) = database.movieCastMemberQueries

    @Factory
    fun movieCrewMemberQueries(database: Database) = database.movieCrewMemberQueries

    @Factory
    fun movieGenreQueries(database: Database) = database.movieGenreQueries

    @Factory
    fun movieKeywordQueries(database: Database) = database.movieKeywordQueries

    @Factory
    fun movieQueries(database: Database) = database.movieQueries

    @Factory
    fun moviePosterQueries(database: Database) = database.moviePosterQueries

    @Factory
    fun movieRatingQueries(database: Database) = database.movieRatingQueries

    @Factory
    fun movieRecommendationQueries(database: Database) = database.movieRecommendationQueries

    @Factory
    fun movieVideoQueries(database: Database) = database.movieVideoQueries

    @Factory
    fun personQueries(database: Database) = database.personQueries

    @Factory
    fun storeFetchDataQueries(database: Database) = database.storeFetchDataQueries

    @Factory
    fun suggestedMovieQueries(database: Database) = database.suggestedMovieQueries

    @Factory
    fun suggestedTvShowQueries(database: Database) = database.suggestedTvShowQueries

    @Factory
    fun tmdbAccountQueries(database: Database) = database.tmdbAccountQueries

    @Factory
    fun tmdbAuthStateQueries(database: Database) = database.tmdbAuthStateQueries

    @Factory
    fun traktAccountQueries(database: Database) = database.traktAccountQueries

    @Factory
    fun traktAuthStateQueries(database: Database) = database.traktAuthStateQueries

    @Factory
    fun tvShowBackdropQueries(database: Database) = database.tvShowBackdropQueries

    @Factory
    fun tvShowCastMemberQueries(database: Database) = database.tvShowCastMemberQueries

    @Factory
    fun tvShowCrewMemberQueries(database: Database) = database.tvShowCrewMemberQueries

    @Factory
    fun tvShowGenreQueries(database: Database) = database.tvShowGenreQueries

    @Factory
    fun tvShowKeywordQueries(database: Database) = database.tvShowKeywordQueries

    @Factory
    fun tvShowPosterQueries(database: Database) = database.tvShowPosterQueries

    @Factory
    fun tvShowQueries(database: Database) = database.tvShowQueries

    @Factory
    fun tvShowRatingQueries(database: Database) = database.tvShowRatingQueries

    @Factory
    fun tvShowRecommendationQueries(database: Database) = database.tvShowRecommendationQueries

    @Factory
    fun tvShowVideoQueries(database: Database) = database.tvShowVideoQueries

    @Factory
    fun tvShowWatchlistQueries(database: Database) = database.tvShowWatchlistQueries

    @Factory
    fun watchlistQueries(database: Database) = database.watchlistQueries
}
