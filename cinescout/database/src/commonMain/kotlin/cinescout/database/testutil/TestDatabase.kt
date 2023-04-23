package cinescout.database.testutil

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import cinescout.database.Database

object TestDatabase {

    fun createDriver() = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)

    fun createDatabase(driver: JdbcSqliteDriver = createDriver()) = with(TestAdapters) {
        Database(
            anticipatedAdapter = AnticipatedAdapter,
            driver = driver,
            fetchDataAdapter = FetchDataAdapter,
            genreAdapter = GenreAdapter,
            keywordAdapter = KeywordAdapter,
            movieAdapter = MovieAdapter,
            personAdapter = PersonAdapter,
            personalRatingAdapter = PersonalRatingAdapter,
            recommendationAdapter = RecommendationAdapter,
            screenplayBackdropAdapter = ScreenplayBackdropAdapter,
            screenplayCastMemberAdapter = ScreenplayCastMemberAdapter,
            screenplayCrewMemberAdapter = ScreenplayCrewMemberAdapter,
            screenplayGenreAdapter = ScreenplayGenreAdapter,
            screenplayKeywordAdapter = ScreenplayKeywordAdapter,
            screenplayPosterAdapter = ScreenplayPosterAdapter,
            screenplayVideoAdapter = ScreenplayVideoAdapter,
            similarAdapter = SimilarAdapter,
            suggestionAdapter = SuggestionAdapter,
            traktAccountAdapter = TraktAccountAdapter,
            traktAuthStateAdapter = TraktAuthStateAdapter,
            tvShowAdapter = TvShowAdapter,
            votingAdapter = VotingAdapter,
            watchlistAdapter = WatchlistAdapter
        )
    }
}
