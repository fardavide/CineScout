package cinescout.movies.data.remote.tmdb

import arrow.core.right
import cinescout.auth.tmdb.domain.usecase.CallWithTmdbAccount
import cinescout.common.model.Rating
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieCreditsMapper
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieImagesMapper
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieKeywordMapper
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieMapper
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieVideosMapper
import cinescout.movies.data.remote.tmdb.model.PostRating
import cinescout.movies.data.remote.tmdb.service.TmdbMovieSearchService
import cinescout.movies.data.remote.tmdb.service.TmdbMovieService
import cinescout.movies.data.remote.tmdb.testdata.DiscoverMoviesResponseTestData
import cinescout.movies.data.remote.tmdb.testdata.GetMovieCreditsResponseTestData
import cinescout.movies.data.remote.tmdb.testdata.GetMovieDetailsResponseTestData
import cinescout.movies.data.remote.tmdb.testdata.GetMovieKeywordsResponseTestData
import cinescout.movies.data.remote.tmdb.testdata.GetRatedMoviesResponseTestData
import cinescout.movies.data.remote.tmdb.testdata.SearchMoviesResponseTestData
import cinescout.movies.domain.sample.DiscoverMoviesParamsSample
import cinescout.movies.domain.sample.MovieCreditsSample
import cinescout.movies.domain.sample.MovieKeywordsSample
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.sample.MovieWithDetailsSample
import cinescout.movies.domain.sample.MovieWithPersonalRatingSample
import cinescout.movies.domain.sample.TmdbMovieIdSample
import cinescout.movies.domain.testdata.MovieImagesTestData
import cinescout.movies.domain.testdata.MovieVideosTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import store.builder.pagedDataOf
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RealTmdbMovieDataSourceTest {

    private val callWithTmdbAccount = CallWithTmdbAccount(
        appScope = TestScope(context = UnconfinedTestDispatcher()),
        isTmdbLinked = mockk {
            every { this@mockk.invoke() } returns flowOf(true)
        }
    )
    private val movieCreditsMapper: TmdbMovieCreditsMapper = mockk {
        every { toMovieCredits(any()) } returns MovieCreditsSample.Inception
    }
    private val movieKeywordMapper: TmdbMovieKeywordMapper = mockk {
        every { toMovieKeywords(any()) } returns MovieKeywordsSample.Inception
    }
    private val movieImagesMapper: TmdbMovieImagesMapper = mockk {
        every { toMovieImages(any()) } returns MovieImagesTestData.Inception
    }
    private val movieMapper: TmdbMovieMapper = mockk {
        every { toMovie(tmdbMovie = any()) } returns MovieSample.Inception
        every { toMovies(tmdbMovies = any()) } returns listOf(MovieSample.Inception)
        every { toMovies(response = any()) } returns listOf(MovieSample.Inception)
        every { toMovieWithDetails(any()) } returns MovieWithDetailsSample.Inception
        every { toMoviesWithRating(any()) } returns listOf(MovieWithPersonalRatingSample.Inception)
    }
    private val movieVideosMapper: TmdbMovieVideosMapper = mockk {
        every { toMovieVideos(any()) } returns MovieVideosTestData.Inception
    }
    private val movieService: TmdbMovieService = mockk {
        coEvery { discoverMovies(any()) } returns DiscoverMoviesResponseTestData.OneMovie.right()
        coEvery { getMovieCredits(any()) } returns GetMovieCreditsResponseTestData.Inception.right()
        coEvery { getMovieDetails(any()) } returns GetMovieDetailsResponseTestData.Inception.right()
        coEvery { getMovieKeywords(any()) } returns GetMovieKeywordsResponseTestData.Inception.right()
        coEvery { getRatedMovies(any()) } returns GetRatedMoviesResponseTestData.OneMovie.right()
        coEvery { postRating(any(), any()) } returns Unit.right()
        coEvery { postToWatchlist(any(), any()) } returns Unit.right()
    }
    private val searchService: TmdbMovieSearchService = mockk {
        coEvery { searchMovie(any(), any()) } returns SearchMoviesResponseTestData.OneMovie.right()
    }
    private val dataSource = RealTmdbMovieDataSource(
        callWithTmdbAccount = callWithTmdbAccount,
        movieCreditsMapper = movieCreditsMapper,
        movieKeywordMapper = movieKeywordMapper,
        movieImagesMapper = movieImagesMapper,
        movieVideosMapper = movieVideosMapper,
        movieMapper = movieMapper,
        movieService = movieService,
        searchService = searchService
    )

    @Test
    fun `discover movies calls service correctly`() = runTest {
        // given
        val params = DiscoverMoviesParamsSample.FromInception

        // when
        dataSource.discoverMovies(params)

        // then
        coVerify { movieService.discoverMovies(params) }
    }

    @Test
    fun `get movie calls service correctly`() = runTest {
        // given
        val movieId = TmdbMovieIdSample.Inception

        // when
        dataSource.getMovieDetails(movieId)

        // then
        coVerify { movieService.getMovieDetails(movieId) }
    }

    @Test
    fun `get movie maps movie correctly`() = runTest {
        // given
        val movieId = TmdbMovieIdSample.Inception
        val expected = MovieWithDetailsSample.Inception.right()

        // when
        val result = dataSource.getMovieDetails(movieId)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get movie credits calls service correctly`() = runTest {
        // given
        val movieId = TmdbMovieIdSample.Inception

        // when
        dataSource.getMovieCredits(movieId)

        // then
        coVerify { movieService.getMovieCredits(movieId) }
    }

    @Test
    fun `get movie keywords maps movie correctly`() = runTest {
        // given
        val movieId = TmdbMovieIdSample.Inception
        val expected = MovieCreditsSample.Inception.right()

        // when
        val result = dataSource.getMovieCredits(movieId)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get movie keywords calls service correctly`() = runTest {
        // given
        val movieId = TmdbMovieIdSample.Inception

        // when
        dataSource.getMovieKeywords(movieId)

        // then
        coVerify { movieService.getMovieKeywords(movieId) }
    }

    @Test
    fun `get movie credits maps movie correctly`() = runTest {
        // given
        val movieId = TmdbMovieIdSample.Inception
        val expected = MovieKeywordsSample.Inception.right()

        // when
        val result = dataSource.getMovieKeywords(movieId)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get rated movies calls service correctly`() = runTest {
        // when
        dataSource.getRatedMovies(1)

        // then
        coVerify { movieService.getRatedMovies(1) }
    }

    @Test
    fun `get rated movies maps correctly`() = runTest {
        // given
        val expected = pagedDataOf(MovieWithPersonalRatingSample.Inception).right()

        // when
        val result = dataSource.getRatedMovies(1)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `post rating does calls service correctly`() = runTest {
        // given
        val movieId = MovieSample.Inception.tmdbId
        Rating.of(8).tap { rating ->

            // when
            dataSource.postRating(movieId, rating)

            // then
            coVerify { movieService.postRating(movieId, PostRating.Request(rating.value)) }
        }
    }

    @Test
    fun `post watchlist does call service`() = runTest {
        // given
        val movieId = MovieSample.Inception.tmdbId

        // when
        dataSource.postAddToWatchlist(movieId)

        // then
        coVerify { movieService.postToWatchlist(movieId, shouldBeInWatchlist = true) }
    }
}
