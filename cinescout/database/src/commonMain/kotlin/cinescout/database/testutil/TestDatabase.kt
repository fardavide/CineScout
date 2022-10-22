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
            likedMovieAdapter = LikedMovieAdapter,
            likedTvShowAdapter = LikedTvShowAdapter,
            movieAdapter = MovieAdapter,
            movieBackdropAdapter = MovieBackdropAdapter,
            movieCastMemberAdapter = MovieCastMemberAdapter,
            movieCrewMemberAdapter = MovieCrewMemberAdapter,
            movieGenreAdapter = MovieGenreAdapter,
            movieKeywordAdapter = MovieKeywordAdapter,
            moviePosterAdapter = MoviePosterAdapter,
            movieRatingAdapter = MovieRatingAdapter,
            movieRecommendationAdapter = MovieRecommendationAdapter,
            movieVideoAdapter = MovieVideoAdapter,
            personAdapter = PersonAdapter,
            storeFetchDataAdapter = StoreFetchDataAdapter,
            suggestedMovieAdapter = SuggestedMovieAdapter,
            suggestedTvShowAdapter = SuggestedTvShowAdapter,
            tmdbAccountAdapter = TmdbAccountAdapter,
            tmdbAuthStateAdapter = TmdbAuthStateAdapter,
            traktAccountAdapter = TraktAccountAdapter,
            traktAuthStateAdapter = TraktAuthStateAdapter,
            tvShowAdapter = TvShowAdapter,
            tvShowBackdropAdapter = TvShowBackdropAdapter,
            tvShowCastMemberAdapter = TvShowCastMemberAdapter,
            tvShowCrewMemberAdapter = TvShowCrewMemberAdapter,
            tvShowGenreAdapter = TvShowGenreAdapter,
            tvShowKeywordAdapter = TvShowKeywordAdapter,
            tvShowPosterAdapter = TvShowPosterAdapter,
            tvShowRatingAdapter = TvShowRatingAdapter,
            tvShowRecommendationAdapter = TvShowRecommendationAdapter,
            tvShowVideoAdapter = TvShowVideoAdapter,
            tvShowWatchlistAdapter = TvShowWatchlistAdapter,
            watchlistAdapter = WatchlistAdapter
        )
    }
}
