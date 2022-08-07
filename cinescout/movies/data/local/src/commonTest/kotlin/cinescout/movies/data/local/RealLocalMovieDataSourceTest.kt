package cinescout.movies.data.local

import cinescout.database.Database
import cinescout.database.testdata.DatabaseMovieTestData
import cinescout.database.testutil.TestDatabase
import cinescout.movies.data.local.mapper.DatabaseMovieCreditsMapper
import cinescout.movies.data.local.mapper.DatabaseMovieMapper
import cinescout.movies.data.local.mapper.toDatabaseId
import cinescout.movies.data.local.mapper.toDatabaseRating
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.testdata.MovieCreditsTestData
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
    private val databaseMovieCreditsMapper: DatabaseMovieCreditsMapper = mockk()
    private val databaseMovieMapper: DatabaseMovieMapper = mockk()
    private val dispatcher = StandardTestDispatcher()
    private val movieCastMemberQueries by lazy { spyk(database.movieCastMemberQueries) }
    private val movieCrewMemberQueries by lazy { spyk(database.movieCrewMemberQueries) }
    private val movieQueries by lazy { spyk(database.movieQueries) }
    private val movieRatingQueries by lazy { spyk(database.movieRatingQueries) }
    private val personQueries by lazy { spyk(database.personQueries) }
    private val watchlistQueries by lazy { spyk(database.watchlistQueries) }
    private val source by lazy {
        RealLocalMovieDataSource(
            databaseMovieCreditsMapper = databaseMovieCreditsMapper,
            databaseMovieMapper = databaseMovieMapper,
            dispatcher = dispatcher,
            movieCastMemberQueries = movieCastMemberQueries,
            movieCrewMemberQueries = movieCrewMemberQueries,
            movieQueries = movieQueries,
            personQueries = personQueries,
            movieRatingQueries = movieRatingQueries,
            watchlistQueries = watchlistQueries
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
        verify { movieQueries.findAllWithPersonalRating() }
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
    fun `find movie credits get from queries`() = runTest {
        // given
        val movieId = TmdbMovieIdTestData.Inception

        // when
        source.findMovieCredits(movieId)

        // then
        verify { movieQueries.findCastByMovieId(movieId.toDatabaseId()) }
        verify { movieQueries.findCrewByMovieId(movieId.toDatabaseId()) }
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
                backdropPath = DatabaseMovieTestData.Inception.backdropPath,
                posterPath = DatabaseMovieTestData.Inception.posterPath,
                ratingAverage = DatabaseMovieTestData.Inception.ratingAverage,
                ratingCount = DatabaseMovieTestData.Inception.ratingCount,
                releaseDate = DatabaseMovieTestData.Inception.releaseDate,
                title = DatabaseMovieTestData.Inception.title,
                tmdbId = DatabaseMovieTestData.Inception.tmdbId
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
            movieQueries.insertMovie(
                backdropPath = DatabaseMovieTestData.Inception.backdropPath,
                posterPath = DatabaseMovieTestData.Inception.posterPath,
                ratingAverage = DatabaseMovieTestData.Inception.ratingAverage,
                ratingCount = DatabaseMovieTestData.Inception.ratingCount,
                releaseDate = DatabaseMovieTestData.Inception.releaseDate,
                title = DatabaseMovieTestData.Inception.title,
                tmdbId = DatabaseMovieTestData.Inception.tmdbId
            )
            movieQueries.insertMovie(
                backdropPath = DatabaseMovieTestData.TheWolfOfWallStreet.backdropPath,
                posterPath = DatabaseMovieTestData.TheWolfOfWallStreet.posterPath,
                ratingAverage = DatabaseMovieTestData.TheWolfOfWallStreet.ratingAverage,
                ratingCount = DatabaseMovieTestData.TheWolfOfWallStreet.ratingCount,
                releaseDate = DatabaseMovieTestData.TheWolfOfWallStreet.releaseDate,
                title = DatabaseMovieTestData.TheWolfOfWallStreet.title,
                tmdbId = DatabaseMovieTestData.TheWolfOfWallStreet.tmdbId
            )
        }
    }

    @Test
    fun `insert movie credits call queries`() = runTest {
        // given
        val credits = MovieCreditsTestData.Inception

        // when
        source.insertCredits(credits)

        // then
        verify {
            personQueries.insertPerson(
                name = credits.cast[0].person.name,
                profileImagePath = credits.cast[0].person.profileImage.orNull()?.path,
                tmdbId = credits.cast[0].person.tmdbId.toDatabaseId()
            )
            personQueries.insertPerson(
                name = credits.cast[1].person.name,
                profileImagePath = credits.cast[1].person.profileImage.orNull()?.path,
                tmdbId = credits.cast[1].person.tmdbId.toDatabaseId()
            )
            personQueries.insertPerson(
                name = credits.crew[0].person.name,
                profileImagePath = credits.crew[0].person.profileImage.orNull()?.path,
                tmdbId = credits.crew[0].person.tmdbId.toDatabaseId()
            )
            movieCastMemberQueries.insertCastMember(
                movieId = credits.movieId.toDatabaseId(),
                personId = credits.cast[0].person.tmdbId.toDatabaseId(),
                character = credits.cast[0].character
            )
            movieCastMemberQueries.insertCastMember(
                movieId = credits.movieId.toDatabaseId(),
                personId = credits.cast[1].person.tmdbId.toDatabaseId(),
                character = credits.cast[1].character
            )
            movieCrewMemberQueries.insertCrewMember(
                movieId = credits.movieId.toDatabaseId(),
                personId = credits.crew[0].person.tmdbId.toDatabaseId(),
                job = credits.crew[0].job
            )
        }
    }

    @Test
    fun `insert rating call queries`() = runTest {
        // given
        val movieId = MovieTestData.Inception.tmdbId
        Rating.of(8).tap { rating ->

            // when
            source.insertRating(movieId, rating)

            // then
            verify {
                movieRatingQueries.insertRating(
                    tmdbId = movieId.toDatabaseId(),
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
                backdropPath = DatabaseMovieTestData.Inception.backdropPath,
                posterPath = DatabaseMovieTestData.Inception.posterPath,
                ratingAverage = DatabaseMovieTestData.Inception.ratingAverage,
                ratingCount = DatabaseMovieTestData.Inception.ratingCount,
                releaseDate = DatabaseMovieTestData.Inception.releaseDate,
                title = DatabaseMovieTestData.Inception.title,
                tmdbId = DatabaseMovieTestData.Inception.tmdbId
            )
            movieQueries.insertMovie(
                backdropPath = DatabaseMovieTestData.TheWolfOfWallStreet.backdropPath,
                posterPath = DatabaseMovieTestData.TheWolfOfWallStreet.posterPath,
                ratingAverage = DatabaseMovieTestData.TheWolfOfWallStreet.ratingAverage,
                ratingCount = DatabaseMovieTestData.TheWolfOfWallStreet.ratingCount,
                releaseDate = DatabaseMovieTestData.TheWolfOfWallStreet.releaseDate,
                title = DatabaseMovieTestData.TheWolfOfWallStreet.title,
                tmdbId = DatabaseMovieTestData.TheWolfOfWallStreet.tmdbId
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
        val movieId = MovieTestData.Inception.tmdbId

        // when
        source.insertWatchlist(movieId)

        // then
        verify {
            watchlistQueries.insertWatchlist(tmdbId = movieId.toDatabaseId())
        }
    }
}
