package cinescout.database

import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class DatabaseQueriesModule {

    @Factory
    fun appSettingsQueries(database: Database) = database.appSettingsQueries

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
    fun recommendationQueries(database: Database) = database.recommendationQueries

    @Factory
    fun storeFetchDataQueries(database: Database) = database.storeFetchDataQueries

    @Factory
    fun suggestionQueries(database: Database) = database.suggestionQueries

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
    fun votingQueries(database: Database) = database.votingQueries

    @Factory
    fun watchlistQueries(database: Database) = database.watchlistQueries
}
