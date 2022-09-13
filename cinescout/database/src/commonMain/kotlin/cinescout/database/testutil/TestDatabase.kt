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
            movieAdapter = MovieAdapter,
            movieBackdropAdapter = MovieBackdropAdapter,
            movieCastMemberAdapter = MovieCastMemberAdapter,
            movieCrewMemberAdapter = MovieCrewMemberAdapter,
            movieGenreAdapter = MovieGenreAdapter,
            movieKeywordAdapter = MovieKeywordAdapter,
            moviePosterAdapter = MoviePosterAdapter,
            movieRatingAdapter = MovieRatingAdapter,
            movieRecommendationAdapter = MovieRecommendationAdapter,
            personAdapter = PersonAdapter,
            storeFetchDataAdapter = StoreFetchDataAdapter,
            suggestedMovieAdapter = SuggestedMovieAdapter,
            tmdbAccountAdapter = TmdbAccountAdapter,
            tmdbAuthStateAdapter = TmdbAuthStateAdapter,
            traktAccountAdapter = TraktAccountAdapter,
            traktAuthStateAdapter = TraktAuthStateAdapter,
            watchlistAdapter = WatchlistAdapter
        )
    }
}
