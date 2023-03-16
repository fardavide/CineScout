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
    fun fetchDataQueries(database: Database) = database.fetchDataQueries

    @Factory
    fun keywordQueries(database: Database) = database.keywordQueries

    @Factory
    fun movieQueries(database: Database) = database.movieQueries

    @Factory
    fun personQueries(database: Database) = database.personQueries

    @Factory
    fun personalRatingQueries(database: Database) = database.personalRatingQueries

    @Factory
    fun recommendationQueries(database: Database) = database.recommendationQueries

    @Factory
    fun screenplayBackdropQueries(database: Database) = database.screenplayBackdropQueries

    @Factory
    fun screenplayCastMemberQueries(database: Database) = database.screenplayCastMemberQueries

    @Factory
    fun screenplayCrewMemberQueries(database: Database) = database.screenplayCrewMemberQueries

    @Factory
    fun screenplayGenreQueries(database: Database) = database.screenplayGenreQueries

    @Factory
    fun screenplayKeywordQueries(database: Database) = database.screenplayKeywordQueries

    @Factory
    fun screenplayQueries(database: Database) = database.screenplayQueries

    @Factory
    fun screenplayPosterQueries(database: Database) = database.screenplayPosterQueries

    @Factory
    fun screenplayVideoQueries(database: Database) = database.screenplayVideoQueries

    @Factory
    fun similarQueries(database: Database) = database.similarQueries

    @Factory
    fun suggestionQueries(database: Database) = database.suggestionQueries

    @Factory
    fun traktAccountQueries(database: Database) = database.traktAccountQueries

    @Factory
    fun traktAuthStateQueries(database: Database) = database.traktAuthStateQueries

    @Factory
    fun tvShowQueries(database: Database) = database.tvShowQueries

    @Factory
    fun votingQueries(database: Database) = database.votingQueries

    @Factory
    fun watchlistQueries(database: Database) = database.watchlistQueries
}
