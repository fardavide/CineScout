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
    fun movieKeywordQueries(database: Database) = database.movieKeywordQueries

    @Factory
    fun movieQueries(database: Database) = database.movieQueries

    @Factory
    fun moviePosterQueries(database: Database) = database.moviePosterQueries

    @Factory
    fun movieVideoQueries(database: Database) = database.movieVideoQueries

    @Factory
    fun personQueries(database: Database) = database.personQueries

    @Factory
    fun personalRatingQueries(database: Database) = database.personalRatingQueries

    @Factory
    fun recommendationQueries(database: Database) = database.recommendationQueries

    @Factory
    fun screenplayCastMemberQueries(database: Database) = database.screenplayCastMemberQueries

    @Factory
    fun screenplayCrewMemberQueries(database: Database) = database.screenplayCrewMemberQueries

    @Factory
    fun screenplayGenreQueries(database: Database) = database.screenplayGenreQueries

    @Factory
    fun similarQueries(database: Database) = database.similarQueries

    @Factory
    fun screenplayQueries(database: Database) = database.screenplayQueries

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
    fun tvShowKeywordQueries(database: Database) = database.tvShowKeywordQueries

    @Factory
    fun tvShowPosterQueries(database: Database) = database.tvShowPosterQueries

    @Factory
    fun tvShowQueries(database: Database) = database.tvShowQueries

    @Factory
    fun tvShowVideoQueries(database: Database) = database.tvShowVideoQueries

    @Factory
    fun votingQueries(database: Database) = database.votingQueries

    @Factory
    fun watchlistQueries(database: Database) = database.watchlistQueries
}
