package cinescout.movies.data.local

import cinescout.database.Database
import cinescout.database.testdata.DatabaseMovieTestData
import cinescout.database.testdata.DatabaseMovieWithRatingTestData
import cinescout.database.testutil.TestDatabase
import cinescout.movies.data.local.mapper.DatabaseMovieCreditsMapper
import cinescout.movies.data.local.mapper.DatabaseMovieMapper
import cinescout.movies.data.local.mapper.DatabaseVideoMapper
import cinescout.movies.data.local.mapper.toDatabaseId
import cinescout.movies.data.local.mapper.toDatabaseRating
import cinescout.movies.domain.model.MovieKeywords
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.testdata.MovieCreditsTestData
import cinescout.movies.domain.testdata.MovieGenresTestData
import cinescout.movies.domain.testdata.MovieKeywordsTestData
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithPersonalRatingTestData
import cinescout.movies.domain.testdata.TmdbMovieIdTestData
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RealLocalMovieDataSourceTest {

    private val driver by lazy { TestDatabase.createDriver() }
    private val database by lazy { TestDatabase.createDatabase(driver) }
    private val databaseMovieCreditsMapper: DatabaseMovieCreditsMapper = mockk()
    private val databaseMovieMapper: DatabaseMovieMapper = mockk()
    private val databaseVideoMapper: DatabaseVideoMapper = mockk()
    private val dispatcher = StandardTestDispatcher()
    private val genreQueries by lazy { spyk(database.genreQueries) }
    private val keywordQueries by lazy { spyk(database.keywordQueries) }
    private val likedMovieQueries by lazy { spyk(database.likedMovieQueries) }
    private val movieBackdropQueries by lazy { spyk(database.movieBackdropQueries) }
    private val movieCastMemberQueries by lazy { spyk(database.movieCastMemberQueries) }
    private val movieCrewMemberQueries by lazy { spyk(database.movieCrewMemberQueries) }
    private val movieGenreQueries by lazy { spyk(database.movieGenreQueries) }
    private val movieKeywordQueries by lazy { spyk(database.movieKeywordQueries) }
    private val movieQueries by lazy { spyk(database.movieQueries) }
    private val moviePosterQueries by lazy { spyk(database.moviePosterQueries) }
    private val movieRatingQueries by lazy { spyk(database.movieRatingQueries) }
    private val movieRecommendationQueries by lazy { spyk(database.movieRecommendationQueries) }
    private val movieVideoQueries by lazy { spyk(database.movieVideoQueries) }
    private val personQueries by lazy { spyk(database.personQueries) }
    private val suggestedMovieQueries by lazy { spyk(database.suggestedMovieQueries) }
    private val watchlistQueries by lazy { spyk(database.watchlistQueries) }

    @OptIn(DelicateCoroutinesApi::class)
    private val source by lazy {
        RealLocalMovieDataSource(
            transacter = database,
            databaseMovieCreditsMapper = databaseMovieCreditsMapper,
            databaseMovieMapper = databaseMovieMapper,
            databaseVideoMapper = databaseVideoMapper,
            readDispatcher = dispatcher,
            genreQueries = genreQueries,
            keywordQueries = keywordQueries,
            likedMovieQueries = likedMovieQueries,
            movieBackdropQueries = movieBackdropQueries,
            movieCastMemberQueries = movieCastMemberQueries,
            movieCrewMemberQueries = movieCrewMemberQueries,
            movieGenreQueries = movieGenreQueries,
            movieKeywordQueries = movieKeywordQueries,
            movieQueries = movieQueries,
            moviePosterQueries = moviePosterQueries,
            movieRatingQueries = movieRatingQueries,
            movieRecommendationQueries = movieRecommendationQueries,
            movieVideoQueries = movieVideoQueries,
            personQueries = personQueries,
            suggestedMovieQueries = suggestedMovieQueries,
            watchlistQueries = watchlistQueries,
            writeDispatcher = newSingleThreadContext("Database write")
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
    fun `find all disliked movies from queries`() = runTest(dispatcher) {
        // when
        source.findAllDislikedMovies()

        // then
        verify { movieQueries.findAllDisliked() }
    }

    @Test
    fun `find all liked movies from queries`() = runTest(dispatcher) {
        // when
        source.findAllLikedMovies()

        // then
        verify { movieQueries.findAllLiked() }
    }

    @Test
    fun `find all rated movies from queries`() = runTest(dispatcher) {
        // when
        source.findAllRatedMovies()

        // then
        verify { movieQueries.findAllWithPersonalRating() }
    }

    @Test
    fun `find all suggested movies from queries`() = runTest(dispatcher) {
        // when
        source.findAllSuggestedMovies()

        // then
        verify { movieQueries.findAllSuggested() }
    }

    @Test
    fun `find movie get from queries`() = runTest(dispatcher) {
        // given
        val movieId = TmdbMovieIdTestData.Inception

        // when
        source.findMovie(movieId)

        // then
        verify { movieQueries.findById(movieId.toDatabaseId()) }
    }

    @Test
    fun `find movie credits get from queries`() = runTest(dispatcher) {
        // given
        val movieId = TmdbMovieIdTestData.Inception

        // when
        source.findMovieCredits(movieId)

        // then
        verify { movieQueries.findCastByMovieId(movieId.toDatabaseId()) }
        verify { movieQueries.findCrewByMovieId(movieId.toDatabaseId()) }
    }

    @Test
    fun `find movie keywords get from queries`() = runTest(dispatcher) {
        // given
        val movieId = TmdbMovieIdTestData.Inception

        // when
        source.findMovieKeywords(movieId)

        // then
        verify { movieQueries.findKeywordsByMovieId(movieId.toDatabaseId()) }
    }

    @Test
    fun `find movie keywords does not return error if there is not stored keywords`() = runTest(dispatcher) {
        // given
        val movieId = TmdbMovieIdTestData.Inception
        val expected = MovieKeywords(movieId, emptyList())

        // when
        val result = source.findMovieKeywords(movieId)
            .first()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `insert one movie call queries`() = runTest(dispatcher) {
        // given
        val movie = MovieTestData.Inception

        // when
        source.insert(movie)

        // then
        verify {
            movieQueries.insertMovie(
                backdropPath = DatabaseMovieTestData.Inception.backdropPath,
                overview = DatabaseMovieTestData.Inception.overview,
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
    fun `insert multiple movies call queries`() = runTest(dispatcher) {
        // given
        val movies = listOf(MovieTestData.Inception, MovieTestData.TheWolfOfWallStreet)
        val databaseMovies = listOf(DatabaseMovieTestData.Inception, DatabaseMovieTestData.TheWolfOfWallStreet)

        // when
        source.insert(movies)

        // then
        verify {
            movieQueries.insertMovie(
                backdropPath = DatabaseMovieTestData.Inception.backdropPath,
                overview = DatabaseMovieTestData.Inception.overview,
                posterPath = DatabaseMovieTestData.Inception.posterPath,
                ratingAverage = DatabaseMovieTestData.Inception.ratingAverage,
                ratingCount = DatabaseMovieTestData.Inception.ratingCount,
                releaseDate = DatabaseMovieTestData.Inception.releaseDate,
                title = DatabaseMovieTestData.Inception.title,
                tmdbId = DatabaseMovieTestData.Inception.tmdbId
            )
            movieQueries.insertMovie(
                backdropPath = DatabaseMovieTestData.TheWolfOfWallStreet.backdropPath,
                overview = DatabaseMovieTestData.TheWolfOfWallStreet.overview,
                posterPath = DatabaseMovieTestData.TheWolfOfWallStreet.posterPath,
                ratingAverage = DatabaseMovieTestData.TheWolfOfWallStreet.ratingAverage,
                ratingCount = DatabaseMovieTestData.TheWolfOfWallStreet.ratingCount,
                releaseDate = DatabaseMovieTestData.TheWolfOfWallStreet.releaseDate,
                title = DatabaseMovieTestData.TheWolfOfWallStreet.title,
                tmdbId = DatabaseMovieTestData.TheWolfOfWallStreet.tmdbId
            )

            for (movie in databaseMovies) {
                movieQueries.insertMovie(
                    backdropPath = movie.backdropPath,
                    overview = movie.overview,
                    posterPath = movie.posterPath,
                    ratingAverage = movie.ratingAverage,
                    ratingCount = movie.ratingCount,
                    releaseDate = movie.releaseDate,
                    title = movie.title,
                    tmdbId = movie.tmdbId
                )
            }
        }
    }

    @Test
    fun `insert disliked calls queries`() = runTest(dispatcher) {
        // given
        val movieId = MovieTestData.Inception.tmdbId

        // when
        source.insertDisliked(movieId)

        // then
        verify {
            likedMovieQueries.insert(tmdbId = movieId.toDatabaseId(), isLiked = false)
        }
    }

    @Test
    fun `insert liked calls queries`() = runTest(dispatcher) {
        // given
        val movieId = MovieTestData.Inception.tmdbId

        // when
        source.insertLiked(movieId)

        // then
        verify {
            likedMovieQueries.insert(tmdbId = movieId.toDatabaseId(), isLiked = true)
        }
    }

    @Test
    fun `insert movie credits call queries`() = runTest(dispatcher) {
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
                character = credits.cast[0].character.orNull(),
                memberOrder = 0
            )
            movieCastMemberQueries.insertCastMember(
                movieId = credits.movieId.toDatabaseId(),
                personId = credits.cast[1].person.tmdbId.toDatabaseId(),
                character = credits.cast[1].character.orNull(),
                memberOrder = 1
            )
            movieCrewMemberQueries.insertCrewMember(
                movieId = credits.movieId.toDatabaseId(),
                personId = credits.crew[0].person.tmdbId.toDatabaseId(),
                job = credits.crew[0].job.orNull(),
                memberOrder = 0
            )
        }
    }

    @Test
    fun `insert genres calls queries`() = runTest(dispatcher) {
        // given
        val genres = MovieGenresTestData.Inception

        // when
        source.insertGenres(genres)

        // then
        verify {
            for (genre in genres.genres) {
                genreQueries.insertGenre(
                    tmdbId = genre.id.toDatabaseId(),
                    name = genre.name
                )
                movieGenreQueries.insertGenre(
                    movieId = genres.movieId.toDatabaseId(),
                    genreId = genre.id.toDatabaseId()
                )
            }
        }
    }

    @Test
    fun `insert keywords calls queries`() = runTest(dispatcher) {
        // given
        val keywords = MovieKeywordsTestData.Inception

        // when
        source.insertKeywords(keywords)

        // then
        verify {
            for (keyword in keywords.keywords) {
                keywordQueries.insertKeyword(
                    tmdbId = keyword.id.toDatabaseId(),
                    name = keyword.name
                )
                movieKeywordQueries.insertKeyword(
                    movieId = keywords.movieId.toDatabaseId(),
                    keywordId = keyword.id.toDatabaseId()
                )
            }
        }
    }

    @Test
    fun `insert rating call queries`() = runTest(dispatcher) {
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
    fun `insert ratings call queries`() = runTest(dispatcher) {
        // given
        val movies = listOf(
            MovieWithPersonalRatingTestData.Inception,
            MovieWithPersonalRatingTestData.TheWolfOfWallStreet
        )
        val databaseMovies =
            listOf(DatabaseMovieWithRatingTestData.Inception, DatabaseMovieWithRatingTestData.TheWolfOfWallStreet)

        // when
        source.insertRatings(movies)

        // then
        verify {
            for (movie in databaseMovies) {
                movieQueries.insertMovie(
                    backdropPath = movie.backdropPath,
                    overview = movie.overview,
                    posterPath = movie.posterPath,
                    ratingAverage = movie.ratingAverage,
                    ratingCount = movie.ratingCount,
                    releaseDate = movie.releaseDate,
                    title = movie.title,
                    tmdbId = movie.tmdbId
                )
                movieRatingQueries.insertRating(
                    tmdbId = movie.tmdbId,
                    rating = movie.personalRating
                )
            }
        }
    }

    @Test
    fun `insert suggested movies calls queries`() = runTest(dispatcher) {
        // given
        val movies = listOf(MovieTestData.Inception, MovieTestData.TheWolfOfWallStreet)

        // when
        source.insertSuggestedMovies(movies)

        // then
        verify {
            for (movie in movies) {
                suggestedMovieQueries.insertSuggestion(
                    tmdbId = movie.tmdbId.toDatabaseId(),
                    affinity = 0.0
                )
            }
        }
    }

    @Test
    fun `insert watchlist calls queries`() = runTest(dispatcher) {
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
