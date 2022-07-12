package cinescout.movies.data

import app.cash.turbine.test
import arrow.core.right
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.testdata.DiscoverMoviesParamsTestData
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithRatingTestData
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RealMovieRepositoryTest {

    private val localMovieDataSource: LocalMovieDataSource = mockk(relaxUnitFun = true)
    private val remoteMovieDataSource: RemoteMovieDataSource = mockk(relaxUnitFun = true) {
        coEvery { discoverMovies(any()) } returns
            listOf(MovieTestData.Inception, MovieTestData.TheWolfOfWallStreet).right()
        coEvery { postRating(any(), any()) } returns Unit.right()
    }
    private val repository = RealMovieRepository(
        localMovieDataSource = localMovieDataSource,
        remoteMovieDataSource = remoteMovieDataSource
    )

    @Test
    fun `add to watchlist inserts locally and post to remote`() = runTest {
        // given
        val movie = MovieTestData.Inception

        // when
        repository.addToWatchlist(movie)

        // then
        coVerifySequence {
            localMovieDataSource.insertWatchlist(movie)
            remoteMovieDataSource.postWatchlist(movie)
        }
    }

    @Test
    fun `discover movies calls local and remote data sources`() = runTest {
        // given
        val movies = listOf(MovieTestData.Inception, MovieTestData.TheWolfOfWallStreet)
        val params = DiscoverMoviesParamsTestData.Random

        // when
        repository.discoverMovies(params).test {

            // then
            assertEquals(movies.right(), awaitItem())
            coVerifySequence {
                remoteMovieDataSource.discoverMovies(params)
                localMovieDataSource.insert(movies)
            }
        }
    }

    @Test
    fun `get all rated movies calls local and remote data sources`() = runTest {
        // given
        val movies = listOf(MovieWithRatingTestData.Inception, MovieWithRatingTestData.TheWolfOfWallStreet)
        every { localMovieDataSource.findAllRatedMovies() } returns flowOf(movies.right())
        coEvery { remoteMovieDataSource.getRatedMovies() } returns movies.right()

        // when
        repository.getAllRatedMovies().test {

            // then
            assertEquals(movies.right(), awaitItem())
            coVerifySequence {
                localMovieDataSource.findAllRatedMovies()
                remoteMovieDataSource.getRatedMovies()
                localMovieDataSource.insertRatings(movies)
            }
        }
    }

    @Test
    fun `get movie calls local and remote data sources`() = runTest {
        // given
        val movie = MovieTestData.Inception
        val movieId = movie.tmdbId
        every { localMovieDataSource.findMovie(movieId) } returns flowOf(movie.right())
        coEvery { remoteMovieDataSource.getMovie(movieId) } returns movie.right()

        // when
        repository.getMovie(movieId).test {

            // then
            assertEquals(movie.right(), awaitItem())
            coVerifySequence {
                localMovieDataSource.findMovie(movieId)
                remoteMovieDataSource.getMovie(movieId)
                localMovieDataSource.insert(movie)
            }
        }
    }

    @Test
    fun `rate movie inserts locally and post to remote`() = runTest {
        // given
        val movie = MovieTestData.Inception
        Rating.of(8).tap { rating ->

            // when
            val result = repository.rate(MovieTestData.Inception, rating)

            // then
            assertEquals(Unit.right(), result)
            coVerifySequence {
                localMovieDataSource.insertRating(movie, rating)
                remoteMovieDataSource.postRating(movie, rating)
            }
        }
    }

}
