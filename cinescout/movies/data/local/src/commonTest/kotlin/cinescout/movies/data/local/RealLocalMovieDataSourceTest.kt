package cinescout.movies.data.local

import cinescout.database.Database
import cinescout.database.testutil.TestDatabase
import cinescout.movies.data.local.mapper.DatabaseMovieMapper
import cinescout.movies.data.local.mapper.toDatabaseId
import cinescout.movies.data.local.mapper.toDatabaseRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithRatingTestData
import cinescout.movies.domain.testdata.TmdbMovieIdTestData
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class RealLocalMovieDataSourceTest {

    private val driver by lazy { TestDatabase.createDriver() }
    private val database by lazy { TestDatabase.createDatabase(driver) }
    private val databaseMovieMapper: DatabaseMovieMapper = mockk()
    private val dispatcher = StandardTestDispatcher()
    private val movieQueries by lazy { spyk(database.movieQueries) }
    private val movieRatingQueries by lazy { spyk(database.movieRatingQueries) }
    private val watchlistQueries by lazy { spyk(database.watchlistQueries) }
    private val source by lazy {
        RealLocalMovieDataSource(
            databaseMovieMapper = databaseMovieMapper,
            dispatcher = dispatcher,
            movieQueries = movieQueries,
            movieRatingQueries = movieRatingQueries,
            watchlistQueries = watchlistQueries,
        )
    }

    @BeforeTest
    fun setup() {
        Database.Schema.create(driver)
    }

    @AfterTest
    fun tearDown() {
        driver.close()
    }

    @Test
    fun `find all rated movies from queries`() = runTest {
        // when
        source.findAllRatedMovies()

        // then
        verify { movieQueries.findAllWithRating() }
    }

    @Test
    fun `find movie get from queries`() = runTest {
        // given
        val movieId = TmdbMovieIdTestData.Inception

        // when
        source.findMovie(movieId)

        // then
        verify { movieQueries.findById(movieId.toDatabaseId()) }
    }

    @Test
    fun `insert one movie call queries`() = runTest {
        // given
        val movie = MovieTestData.Inception

        // when
        source.insert(movie)

        // then
        verify {
            movieQueries.insertMovie(
                tmdbId = movie.tmdbId.toDatabaseId(),
                releaseDate = movie.releaseDate,
                title = movie.title
            )
        }
    }

    @Test
    fun `insert multiple movies call queries`() = runTest {
        // given
        val movies = listOf(MovieTestData.Inception, MovieTestData.TheWolfOfWallStreet)

        // when
        source.insert(movies)

        // then
        verify {
            for (movie in movies) {
                movieQueries.insertMovie(
                    tmdbId = movie.tmdbId.toDatabaseId(),
                    releaseDate = movie.releaseDate,
                    title = movie.title
                )
            }
        }
    }

    @Test
    fun `insert rating call queries`() = runTest {
        // given
        val movie = MovieTestData.Inception
        Rating.of(8).tap { rating ->

            // when
            source.insertRating(movie, rating)

            // then
            verify {
                movieQueries.insertMovie(
                    tmdbId = movie.tmdbId.toDatabaseId(),
                    releaseDate = movie.releaseDate,
                    title = movie.title
                )
                movieRatingQueries.insertRating(
                    tmdbId = movie.tmdbId.toDatabaseId(),
                    rating = rating.toDatabaseRating()
                )
            }
        }
    }

    @Test
    fun `insert ratings call queries`() = runTest {
        // given
        val movies = listOf(MovieWithRatingTestData.Inception, MovieWithRatingTestData.TheWolfOfWallStreet)

        // when
        source.insertRatings(movies)

        // then
        verify {
            movieQueries.insertMovie(
                tmdbId = MovieWithRatingTestData.Inception.movie.tmdbId.toDatabaseId(),
                releaseDate = MovieWithRatingTestData.Inception.movie.releaseDate,
                title = MovieWithRatingTestData.Inception.movie.title
            )
            movieQueries.insertMovie(
                tmdbId = MovieWithRatingTestData.TheWolfOfWallStreet.movie.tmdbId.toDatabaseId(),
                releaseDate = MovieWithRatingTestData.TheWolfOfWallStreet.movie.releaseDate,
                title = MovieWithRatingTestData.TheWolfOfWallStreet.movie.title
            )
            movieRatingQueries.insertRating(
                tmdbId = MovieWithRatingTestData.Inception.movie.tmdbId.toDatabaseId(),
                rating = MovieWithRatingTestData.Inception.rating.toDatabaseRating()
            )
            movieRatingQueries.insertRating(
                tmdbId = MovieWithRatingTestData.TheWolfOfWallStreet.movie.tmdbId.toDatabaseId(),
                rating = MovieWithRatingTestData.TheWolfOfWallStreet.rating.toDatabaseRating()
            )
        }
    }

    @Test
    fun `insert watchlist calls queries`() = runTest {
        // given
        val movie = MovieTestData.Inception

        // when
        source.insertWatchlist(movie)

        // then
        verify {
            movieQueries.insertMovie(
                tmdbId = movie.tmdbId.toDatabaseId(),
                releaseDate = movie.releaseDate,
                title = movie.title
            )
            watchlistQueries.insertWatchlist(tmdbId = movie.tmdbId.toDatabaseId())
        }
    }
}
