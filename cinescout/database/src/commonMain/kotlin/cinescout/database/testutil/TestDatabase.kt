package cinescout.database.testutil

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import cinescout.database.Database

object TestDatabase {

    fun createDriver() = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)

    fun createDatabase(driver: JdbcSqliteDriver) = with(TestAdapters) {
        Database(
            driver = driver,
            genreAdapter = GenreAdapter,
            keywordAdapter = KeywordAdapter,
            movieAdapter = MovieAdapter,
            movieBackdropAdapter = MovieBackdropAdapter,
            movieKeywordAdapter = MovieKeywordAdapter,
            moviePosterAdapter = MoviePosterAdapter,
            movieVideoAdapter = MovieVideoAdapter,
            personAdapter = PersonAdapter,
            personalRatingAdapter = PersonalRatingAdapter,
            recommendationAdapter = RecommendationAdapter,
            screenplayCastMemberAdapter = ScreenplayCastMemberAdapter,
            screenplayCrewMemberAdapter = ScreenplayCrewMemberAdapter,
            screenplayGenreAdapter = ScreenplayGenreAdapter,
            similarAdapter = SimilarAdapter,
            storeFetchDataAdapter = StoreFetchDataAdapter,
            suggestionAdapter = SuggestionAdapter,
            traktAccountAdapter = TraktAccountAdapter,
            traktAuthStateAdapter = TraktAuthStateAdapter,
            tvShowAdapter = TvShowAdapter,
            tvShowBackdropAdapter = TvShowBackdropAdapter,
            tvShowKeywordAdapter = TvShowKeywordAdapter,
            tvShowPosterAdapter = TvShowPosterAdapter,
            tvShowVideoAdapter = TvShowVideoAdapter,
            votingAdapter = VotingAdapter,
            watchlistAdapter = WatchlistAdapter
        )
    }
}
