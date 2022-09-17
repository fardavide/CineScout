package cinescout.movies.data.remote.tmdb.service

import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import cinescout.movies.data.remote.testdata.TmdbMovieTestData
import cinescout.movies.data.remote.tmdb.model.PostRating
import cinescout.movies.data.remote.tmdb.testdata.DiscoverMoviesResponseTestData
import cinescout.movies.data.remote.tmdb.testdata.GetMovieCreditsResponseTestData
import cinescout.movies.data.remote.tmdb.testdata.GetMovieDetailsResponseTestData
import cinescout.movies.data.remote.tmdb.testdata.GetMovieKeywordsResponseTestData
import cinescout.movies.data.remote.tmdb.testdata.GetRatedMoviesResponseTestData
import cinescout.movies.data.remote.tmdb.testutil.MockTmdbMovieEngine
import cinescout.movies.domain.testdata.DiscoverMoviesParamsTestData
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.network.CineScoutClient
import cinescout.network.tmdb.TmdbAuthProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TmdbMovieServiceTest {

    private val authProvider: TmdbAuthProvider = mockk()
    private val engine = MockTmdbMovieEngine()
    private val client = CineScoutClient(engine)
    private val service = TmdbMovieService(authProvider = authProvider, client = client)

    @Test
    fun `discover movies returns right movies`() = runTest {
        // given
        val expected = DiscoverMoviesResponseTestData.TwoMovies.right()

        // when
        val result = service.discoverMovies(DiscoverMoviesParamsTestData.FromInception)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `discover movies uses cast, crew, genres, keyword and release year`() = runTest {
        // given
        val params = DiscoverMoviesParamsTestData.FromInception

        // when
        service.discoverMovies(params)

        // then
        val parameters = engine.requestHistory.last().url.parameters
        assertEquals(params.castMember.orNull()?.person?.tmdbId?.value?.toString(), parameters["with_cast"])
        assertEquals(params.crewMember.orNull()?.person?.tmdbId?.value?.toString(), parameters["with_crew"])
        assertEquals(params.genre.orNull()?.id?.value?.toString(), parameters["with_genres"])
        assertEquals(params.keyword.orNull()?.id?.value?.toString(), parameters["with_keywords"])
        assertEquals(params.releaseYear.orNull()?.value?.toString(), parameters["primary_release_year"])
    }

    @Test
    fun `get movie returns right movie`() = runTest {
        // given
        val movie = GetMovieDetailsResponseTestData.Inception
        val expected = movie.right()

        // when
        val result = service.getMovieDetails(movie.id)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get movie credits returns right credits`() = runTest {
        // given
        val credits = GetMovieCreditsResponseTestData.Inception
        val expected = credits.right()

        // when
        val result = service.getMovieCredits(credits.movieId)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get movie keywords returns right keywords`() = runTest {
        // given
        val keywords = GetMovieKeywordsResponseTestData.Inception
        val expected = keywords.right()

        // when
        val result = service.getMovieKeywords(MovieTestData.Inception.tmdbId)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get rated movies returns error if not authenticated`() = runTest {
        // given
        val expected = NetworkError.Unauthorized.left()
        coEvery { authProvider.accountId() } returns null

        // when
        val result = service.getRatedMovies(1)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get rated movies returns result if authenticated`() = runTest {
        // given
        val expected = GetRatedMoviesResponseTestData.OneMovie.right()
        coEvery { authProvider.accountId() } returns "123"

        // when
        val result = service.getRatedMovies(1)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `post rating returns success`() = runTest {
        // given
        val movie = TmdbMovieTestData.Inception
        val expected = Unit.right()

        // when
        val result = service.postRating(movie.id, PostRating.Request(8.0))

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `post watchlist returns success if authenticated`() = runTest {
        // given
        val movie = TmdbMovieTestData.Inception
        val expected = Unit.right()
        coEvery { authProvider.accountId() } returns "123"

        // when
        val result = service.postToWatchlist(movie.id, true)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `post watchlist returns error if not authenticated`() = runTest {
        // given
        val movie = TmdbMovieTestData.Inception
        val expected = NetworkError.Unauthorized.left()
        coEvery { authProvider.accountId() } returns null

        // when
        val result = service.postToWatchlist(movie.id, true)

        // then
        assertEquals(expected, result)
    }
}
