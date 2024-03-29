package cinescout.database

import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class DatabaseQueriesModule {

    @Factory
    fun anticipatedQueries(database: Database) = database.anticipatedQueries

    @Factory
    fun appSettingsQueries(database: Database) = database.appSettingsQueries

    @Factory
    fun episodeQueries(database: Database) = database.episodeQueries

    @Factory
    fun fetchDataQueries(database: Database) = database.fetchDataQueries

    @Factory
    fun genreQueries(database: Database) = database.genreQueries

    @Factory
    fun historyQueries(database: Database) = database.historyQueries

    @Factory
    fun keywordQueries(database: Database) = database.keywordQueries

    @Factory
    fun movieQueries(database: Database) = database.movieQueries

    @Factory
    fun personQueries(database: Database) = database.personQueries

    @Factory
    fun personalRatingQueries(database: Database) = database.personalRatingQueries

    @Factory
    fun popularQueries(database: Database) = database.popularQueries

    @Factory
    fun recommendationQueries(database: Database) = database.recommendationQueries

    @Factory
    fun recommendedQueries(database: Database) = database.recommendedQueries

    @Factory
    fun screenplayBackdropQueries(database: Database) = database.screenplayBackdropQueries

    @Factory
    fun screenplayCastMemberQueries(database: Database) = database.screenplayCastMemberQueries

    @Factory
    fun screenplayCrewMemberQueries(database: Database) = database.screenplayCrewMemberQueries

    @Factory
    fun screenplayFindAnticipatedQueries(database: Database) = database.screenplayFindAnticipatedQueries

    @Factory
    fun screenplayFindDislikedQueries(database: Database) = database.screenplayFindDislikedQueries

    @Factory
    fun screenplayFindInProgressQueries(database: Database) = database.screenplayFindInProgressQueries

    @Factory
    fun screenplayFindLikedQueries(database: Database) = database.screenplayFindLikedQueries

    @Factory
    fun screenplayFindWatchlistQueries(database: Database) = database.screenplayFindWatchlistQueries

    @Factory
    fun screenplayFindWithGenreSlugsQueries(database: Database) = database.screenplayFindWithGenreSlugsQueries

    @Factory
    fun screenplayFindWithPersonalRatingQueries(database: Database) =
        database.screenplayFindWithPersonalRatingQueries

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
    fun seasonQueries(database: Database) = database.seasonQueries

    @Factory
    fun similarQueries(database: Database) = database.similarQueries

    @Factory
    fun suggestionQueries(database: Database) = database.suggestionQueries

    @Factory
    fun syncQueries(database: Database) = database.syncQueries

    @Factory
    fun traktAccountQueries(database: Database) = database.traktAccountQueries

    @Factory
    fun traktAuthStateQueries(database: Database) = database.traktAuthStateQueries

    @Factory
    fun trendingQueries(database: Database) = database.trendingQueries

    @Factory
    fun tvShowQueries(database: Database) = database.tvShowQueries

    @Factory
    fun votingQueries(database: Database) = database.votingQueries

    @Factory
    fun watchlistQueries(database: Database) = database.watchlistQueries
}
