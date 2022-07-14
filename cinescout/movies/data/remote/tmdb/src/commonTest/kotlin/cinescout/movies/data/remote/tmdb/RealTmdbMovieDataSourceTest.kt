package cinescout.movies.data.remote.tmdb

import arrow.core.right
import cinescout.model.pagedDataOf
import cinescout.movies.data.remote.testdata.TmdbMovieTestData
import cinescout.movies.data.remote.tmdb.mapper.TmdbMovieMapper
import cinescout.movies.data.remote.tmdb.model.PostRating
import cinescout.movies.data.remote.tmdb.service.TmdbMovieService
import cinescout.movies.data.remote.tmdb.testdata.DiscoverMoviesResponseTestData
import cinescout.movies.data.remote.tmdb.testdata.GetRatedMoviesResponseTestData
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.testdata.DiscoverMoviesParamsTestData
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithRatingTestData
import cinescout.movies.domain.testdata.TmdbMovieIdTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RealTmdbMovieDataSourceTest {

    private val movieMapper: TmdbMovieMapper = mockk {
        every { toMovie(any()) } returns MovieTestData.Inception
        every { toMovies(any()) } returns listOf(MovieTestData.Inception)
        every { toMoviesWithRating(any()) } returns listOf(MovieWithRatingTestData.Inception)
    }
    private val service: TmdbMovieService = mockk {
        coEvery { discoverMovies(any()) } returns DiscoverMoviesResponseTestData.OneMovie.right()
        coEvery { getMovie(any()) } returns TmdbMovieTestData.Inception.right()
        coEvery { getRatedMovies() } returns GetRatedMoviesResponseTestData.OneMovie.right()
        coEvery { postRating(any(), any()) } returns Unit.right()
    }
    private val dataSource = RealTmdbMovieDataSource(movieMapper = movieMapper, movieService = service)

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
    fun `get rated movies calls service correctly`() = runTest {
        // when
        dataSource.getRatedMovies()

        // then
        coVerify { service.getRatedMovies() }
    }

    @Test
    fun `get rated movies maps correctly`() = runTest {
        // given
        val expected = pagedDataOf(MovieWithRatingTestData.Inception).right()

        // when
        val result = dataSource.getRatedMovies()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `post watchlist does nothing`() = runTest {
        // given
        val movie = MovieTestData.Inception

        // when
        dataSource.postWatchlist(movie)

        // then TODO
    }

    @Test
    fun `post rating does calls service correctly`() = runTest {
        // given
        val movie = MovieTestData.Inception
        Rating.of(8).tap { rating ->

            // when
            dataSource.postRating(movie, rating)

            // then
            coVerify { service.postRating(movie.tmdbId, PostRating.Request(rating.value)) }
        }
    }
}
