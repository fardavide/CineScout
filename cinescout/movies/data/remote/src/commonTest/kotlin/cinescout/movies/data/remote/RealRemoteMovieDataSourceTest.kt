package cinescout.movies.data.remote

import arrow.core.right
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.testdata.DiscoverMoviesParamsTestData
import cinescout.movies.domain.testdata.MovieRatingTestData
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithRatingTestData
import cinescout.movies.domain.testdata.TmdbMovieIdTestData
import cinescout.store.PagedData
import cinescout.store.Paging
import cinescout.store.pagedDataOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RealRemoteMovieDataSourceTest {

    private val tmdbSource: TmdbRemoteMovieDataSource = mockk(relaxUnitFun = true) {
        coEvery { postRating(any(), any()) } returns Unit.right()
    }
    private val traktSource: TraktRemoteMovieDataSource = mockk(relaxUnitFun = true)
    private val remoteMovieDataSource = RealRemoteMovieDataSource(tmdbSource = tmdbSource, traktSource = traktSource)

    @Test
    fun `discover movies returns the right movies from Tmdb`() = runTest {
        // given
        val expected = listOf(MovieTestData.Inception, MovieTestData.TheWolfOfWallStreet).right()
        val params = DiscoverMoviesParamsTestData.Random
        coEvery { tmdbSource.discoverMovies(params) } returns expected

        // when
        val result = remoteMovieDataSource.discoverMovies(params)

        // then
        assertEquals(expected, result)
        coVerify { tmdbSource.discoverMovies(params) }
    }

    @Test
    fun `get movie returns the right movie from Tmdb`() = runTest {
        // given
        val expected = MovieTestData.Inception.right()
        val movieId = TmdbMovieIdTestData.Inception
        coEvery { tmdbSource.getMovie(movieId) } returns expected

        // when
        val result = remoteMovieDataSource.getMovie(movieId)

        // then
        assertEquals(expected, result)
        coVerify { tmdbSource.getMovie(movieId) }
    }

    @Test
    fun `get rated movies return the right ratings from Tmdb and Trakt`() = runTest {
        // given
        val expected = PagedData.Remote(
            data = listOf(
                MovieWithRatingTestData.Inception,
                MovieWithRatingTestData.TheWolfOfWallStreet,
                MovieWithRatingTestData.War
            ),
            paging = Paging.Page.MultipleSources(
                page = 2,
                totalPages = 2,
            )
        ).right()
        coEvery { tmdbSource.getMovie(TmdbMovieIdTestData.TheWolfOfWallStreet) } returns
            MovieTestData.TheWolfOfWallStreet.right()
        coEvery { tmdbSource.getMovie(TmdbMovieIdTestData.War) } returns
            MovieTestData.War.right()
        coEvery { tmdbSource.getRatedMovies() } returns
            pagedDataOf(MovieWithRatingTestData.Inception, MovieWithRatingTestData.TheWolfOfWallStreet).right()
        coEvery { traktSource.getRatedMovies() } returns
            pagedDataOf(MovieRatingTestData.TheWolfOfWallStreet, MovieRatingTestData.War).right()

        // when
        val result = remoteMovieDataSource.getRatedMovies()

        // then
        assertEquals(
            expected,
            result,
            message =
            """Expected ${expected.orNull()!!.data.map { it.movie.title } }
              |but was  ${result.orNull()!!.data.map { it.movie.title } }
              |""".trimMargin()
        )
    }

    @Test
    fun `post rating posts to tmdb and trakt`() = runTest {
        // given
        val movie = MovieTestData.Inception

        // when
        Rating.of(8).tap { rating ->
            val result = remoteMovieDataSource.postRating(movie, rating)

            // then
            assertEquals(Unit.right(), result)
            coVerify { tmdbSource.postRating(movie, rating) }
            coVerify { traktSource.postRating(movie, rating) }
        }
    }

    @Test
    fun `post watchlist posts to tmdb and trakt`() = runTest {
        // given
        val movie = MovieTestData.Inception

        // when
        remoteMovieDataSource.postWatchlist(movie)

        // then
        coVerify { tmdbSource.postWatchlist(movie) }
        coVerify { traktSource.postWatchlist(movie) }
    }
}
