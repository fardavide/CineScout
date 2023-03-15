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
            movieCastMemberAdapter = MovieCastMemberAdapter,
            movieCrewMemberAdapter = MovieCrewMemberAdapter,
            movieGenreAdapter = MovieGenreAdapter,
            movieKeywordAdapter = MovieKeywordAdapter,
            moviePosterAdapter = MoviePosterAdapter,
            movieRecommendationAdapter = MovieRecommendationAdapter,
            movieVideoAdapter = MovieVideoAdapter,
            personAdapter = PersonAdapter,
            personalRatingAdapter = PersonalRatingAdapter,
            recommendationAdapter = RecommendationAdapter,
            storeFetchDataAdapter = StoreFetchDataAdapter,
            suggestionAdapter = SuggestionAdapter,
            traktAccountAdapter = TraktAccountAdapter,
            traktAuthStateAdapter = TraktAuthStateAdapter,
            tvShowAdapter = TvShowAdapter,
            tvShowBackdropAdapter = TvShowBackdropAdapter,
            tvShowCastMemberAdapter = TvShowCastMemberAdapter,
            tvShowCrewMemberAdapter = TvShowCrewMemberAdapter,
            tvShowGenreAdapter = TvShowGenreAdapter,
            tvShowKeywordAdapter = TvShowKeywordAdapter,
            tvShowPosterAdapter = TvShowPosterAdapter,
            tvShowRecommendationAdapter = TvShowRecommendationAdapter,
            tvShowVideoAdapter = TvShowVideoAdapter,
            votingAdapter = VotingAdapter,
            watchlistAdapter = WatchlistAdapter
        )
    }
}
