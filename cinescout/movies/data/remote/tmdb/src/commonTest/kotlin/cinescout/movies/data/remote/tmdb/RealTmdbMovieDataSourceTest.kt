package cinescout.movies.data.remote.tmdb

import arrow.core.right
import cinescout.movies.data.remote.testdata.TmdbMovieTestData
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieCreditsMapper
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieMapper
import cinescout.movies.data.remote.tmdb.model.PostRating
import cinescout.movies.data.remote.tmdb.service.TmdbMovieService
import cinescout.movies.data.remote.tmdb.testdata.DiscoverMoviesResponseTestData
import cinescout.movies.data.remote.tmdb.testdata.GetMovieCreditsResponseTestData
import cinescout.movies.data.remote.tmdb.testdata.GetRatedMoviesResponseTestData
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.testdata.DiscoverMoviesParamsTestData
import cinescout.movies.domain.testdata.MovieCreditsTestData
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithRatingTestData
import cinescout.movies.domain.testdata.TmdbMovieIdTestData
import cinescout.store.pagedDataOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RealTmdbMovieDataSourceTest {

    private val movieCreditsMapper: TmdbMovieCreditsMapper = mockk {
        every { toMovieCredits(any()) } returns MovieCreditsTestData.Inception
    }
    private val movieMapper: TmdbMovieMapper = mockk {
        every { toMovie(any()) } returns MovieTestData.Inception
        every { toMovies(any()) } returns listOf(MovieTestData.Inception)
        every { toMoviesWithRating(any()) } returns listOf(MovieWithRatingTestData.Inception)
    }
    private val service: TmdbMovieService = mockk {
        coEvery { discoverMovies(any()) } returns DiscoverMoviesResponseTestData.OneMovie.right()
        coEvery { getMovie(any()) } returns TmdbMovieTestData.Inception.right()
        coEvery { getMovieCredits(any()) } returns GetMovieCreditsResponseTestData.Inception.right()
        coEvery { getRatedMovies(any()) } returns GetRatedMoviesResponseTestData.OneMovie.right()
        coEvery { postRating(any(), any()) } returns Unit.right()
        coEvery { postToWatchlist(any(), any()) } returns Unit.right()
    }
    private val dataSource = RealTmdbMovieDataSource(
        movieCreditsMapper = movieCreditsMapper,
        movieMapper = movieMapper,
        movieService = service
    )

    @Test
    fun `discover movies calls service correctly`() = runTest {
        // given
        val params = DiscoverMoviesParamsTestData.Random

        // when
        dataSource.discoverMovies(params)

        // then
        coVerify { service.discoverMovies(params) }
    }

    @Test
    fun `get movie calls service correctly`() = runTest {
        // given
        val movieId = TmdbMovieIdTestData.Inception

        // when
        dataSource.getMovie(movieId)

        // then
        coVerify { service.getMovie(movieId) }
    }

    @Test
    fun `get movie maps movie correctly`() = runTest {
        // given
        val movieId = TmdbMovieIdTestData.Inception
        val expected = MovieTestData.Inception.right()

        // when
        val result = dataSource.getMovie(movieId)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get movie credits calls service correctly`() = runTest {
        // given
        val movieId = TmdbMovieIdTestData.Inception

        // when
        dataSource.getMovieCredits(movieId)

        // then
        coVerify { service.getMovieCredits(movieId) }
    }

    @Test
    fun `get movie credits maps movie correctly`() = runTest {
        // given
        val movieId = TmdbMovieIdTestData.Inception
        val expected = MovieCreditsTestData.Inception.right()

        // when
        val result = dataSource.getMovieCredits(movieId)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get rated movies calls service correctly`() = runTest {
        // when
        dataSource.getRatedMovies(1)

        // then
        coVerify { service.getRatedMovies(1) }
    }

    @Test
    fun `get rated movies maps correctly`() = runTest {
        // given
        val expected = pagedDataOf(MovieWithRatingTestData.Inception).right()

        // when
        val result = dataSource.getRatedMovies(1)

        // then
        assertEquals(expected, result)
    }

    @Test
    @Ignore("not implemented")
    fun `post disliked does call service`() = runTest {
        // given
        val movieId = MovieTestData.Inception.tmdbId

        // when
        dataSource.postDisliked(movieId)

        // then
        // TODO coVerify { service.postDisliked(movieId) }
    }

    @Test
    @Ignore("not implemented")
    fun `post liked does call service`() = runTest {
        // given
        val movieId = MovieTestData.Inception.tmdbId

        // when
        dataSource.postLiked(movieId)

        // then
        // TODO coVerify { service.postLiked(movieId) }
    }

    @Test
    fun `post rating does calls service correctly`() = runTest {
        // given
        val movieId = MovieTestData.Inception.tmdbId
        Rating.of(8).tap { rating ->

            // when
            dataSource.postRating(movieId, rating)

            // then
            coVerify { service.postRating(movieId, PostRating.Request(rating.value)) }
        }
    }

    @Test
    fun `post watchlist does call service`() = runTest {
        // given
        val movieId = MovieTestData.Inception.tmdbId

        // when
        dataSource.postAddToWatchlist(movieId)

        // then
        coVerify { service.postToWatchlist(movieId, shouldBeInWatchlist = true) }
    }
}
