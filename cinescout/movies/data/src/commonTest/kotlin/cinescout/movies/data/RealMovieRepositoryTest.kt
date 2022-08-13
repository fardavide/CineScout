package cinescout.movies.data

import app.cash.turbine.test
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.testdata.DiscoverMoviesParamsTestData
import cinescout.movies.domain.testdata.MovieCreditsTestData
import cinescout.movies.domain.testdata.MovieKeywordsTestData
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithDetailsTestData
import cinescout.movies.domain.testdata.MovieWithPersonalRatingTestData
import cinescout.store.Paging
import cinescout.store.toPagedData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RealMovieRepositoryTest {

    private val localMovieDataSource: LocalMovieDataSource = mockk(relaxUnitFun = true)
    private val remoteMovieDataSource: RemoteMovieDataSource = mockk(relaxUnitFun = true) {
        coEvery { discoverMovies(any()) } returns
            listOf(MovieTestData.Inception, MovieTestData.TheWolfOfWallStreet).right()
        coEvery { postAddToWatchlist(any()) } returns Unit.right()
        coEvery { postRating(any(), any()) } returns Unit.right()
    }
    private val repository = RealMovieRepository(
        localMovieDataSource = localMovieDataSource,
        remoteMovieDataSource = remoteMovieDataSource
    )

    @Test
    fun `add to disliked inserts locally and post to remote`() = runTest {
        // given
        val movieId = MovieTestData.Inception.tmdbId

        // when
        repository.addToDisliked(movieId)

        // then
        coVerifySequence {
            localMovieDataSource.insertDisliked(movieId)
        }
    }

    @Test
    fun `add to liked inserts locally and post to remote`() = runTest {
        // given
        val movieId = MovieTestData.Inception.tmdbId

        // when
        repository.addToLiked(movieId)

        // then
        coVerifySequence {
            localMovieDataSource.insertLiked(movieId)
        }
    }

    @Test
    fun `add to watchlist inserts locally and post to remote`() = runTest {
        // given
        val movieId = MovieTestData.Inception.tmdbId

        // when
        repository.addToWatchlist(movieId)

        // then
        coVerifySequence {
            localMovieDataSource.insertWatchlist(movieId)
            remoteMovieDataSource.postAddToWatchlist(movieId)
        }
    }

    @Test
    fun `add movie to watchlist inserts locally and post to remote`() = runTest {
        // given
        val movieId = MovieTestData.Inception.tmdbId

        // when
        repository.addToWatchlist(movieId)

        // then
        coVerifySequence {
            localMovieDataSource.insertWatchlist(movieId)
            remoteMovieDataSource.postAddToWatchlist(movieId)
        }
    }

    @Test
    fun `discover movies calls local and remote data sources`() = runTest {
        // given
        val movies = listOf(MovieTestData.Inception, MovieTestData.TheWolfOfWallStreet)
        val params = DiscoverMoviesParamsTestData.FromInception

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
    fun `get all disliked movies calls local data sources`() = runTest {
        // given
        val movies = listOf(MovieTestData.Inception, MovieTestData.TheWolfOfWallStreet)
        every { localMovieDataSource.findAllDislikedMovies() } returns flowOf(movies.right())

        // when
        repository.getAllDislikedMovies().test {

            // then
            assertEquals(movies.right(), awaitItem())
            cancelAndIgnoreRemainingEvents()
            coVerifySequence {
                localMovieDataSource.findAllDislikedMovies()
            }
        }
    }

    @Test
    fun `get all liked movies calls local data sources`() = runTest {
        // given
        val movies = listOf(MovieTestData.Inception, MovieTestData.TheWolfOfWallStreet)
        every { localMovieDataSource.findAllLikedMovies() } returns flowOf(movies.right())

        // when
        repository.getAllLikedMovies().test {

            // then
            assertEquals(movies.right(), awaitItem())
            cancelAndIgnoreRemainingEvents()
            coVerifySequence {
                localMovieDataSource.findAllLikedMovies()
            }
        }
    }

    @Test
    fun `get all rated movies calls local and remote data sources`() = runTest {
        // given
        val movies = listOf(
            MovieWithPersonalRatingTestData.Inception,
            MovieWithPersonalRatingTestData.TheWolfOfWallStreet
        )
        val pagedMovies = movies.toPagedData(Paging.Page.DualSources.Initial)
        every { localMovieDataSource.findAllRatedMovies() } returns flowOf(movies.right())
        coEvery { remoteMovieDataSource.getRatedMovies(any()) } returns pagedMovies.right()

        // when
        repository.getAllRatedMovies().test {

            // then
            assertEquals(movies.right(), awaitItem().map { it.data })
            coVerifySequence {
                localMovieDataSource.findAllRatedMovies()
                remoteMovieDataSource.getRatedMovies(any())
                localMovieDataSource.insertRatings(movies)
            }
        }
    }

    @Test
    fun `get all watchlist movies calls local and remote data sources`() = runTest {
        // given
        val movies = listOf(
            MovieTestData.Inception,
            MovieTestData.TheWolfOfWallStreet
        )
        val pagedMovies = movies.toPagedData(Paging.Page.DualSources.Initial)
        every { localMovieDataSource.findAllWatchlistMovies() } returns flowOf(movies.right())
        coEvery { remoteMovieDataSource.getWatchlistMovies(any()) } returns pagedMovies.right()

        // when
        repository.getAllWatchlistMovies().test {

            // then
            assertEquals(movies.right(), awaitItem().map { it.data })
            coVerifySequence {
                localMovieDataSource.findAllWatchlistMovies()
                remoteMovieDataSource.getWatchlistMovies(any())
                localMovieDataSource.insertWatchlist(movies)
            }
        }
    }

    @Test
    fun `get movie calls local and remote data sources`() = runTest {
        // given
        val movie = MovieWithDetailsTestData.Inception
        val movieId = movie.movie.tmdbId
        every { localMovieDataSource.findMovieWithDetails(movieId) } returns flowOf(movie.right())
        coEvery { remoteMovieDataSource.getMovieDetails(movieId) } returns movie.right()

        // when
        repository.getMovieDetails(movieId).test {

            // then
            assertEquals(movie.right(), awaitItem())
            cancelAndConsumeRemainingEvents()
            coVerifySequence {
                localMovieDataSource.findMovieWithDetails(movieId)
                remoteMovieDataSource.getMovieDetails(movieId)
                localMovieDataSource.insert(movie)
            }
        }
    }

    @Test
    fun `get movie credits calls local and remote data sources`() = runTest {
        // given
        val credits = MovieCreditsTestData.Inception
        val movieId = credits.movieId
        every { localMovieDataSource.findMovieCredits(movieId) } returns flowOf(credits.right())
        coEvery { remoteMovieDataSource.getMovieCredits(movieId) } returns credits.right()

        // when
        repository.getMovieCredits(movieId).test {

            // then
            assertEquals(credits.right(), awaitItem())
            cancelAndConsumeRemainingEvents()
            coVerifySequence {
                localMovieDataSource.findMovieCredits(movieId)
                remoteMovieDataSource.getMovieCredits(movieId)
                localMovieDataSource.insertCredits(credits)
            }
        }
    }

    @Test
    fun `get movie keywords calls local and remote data sources`() = runTest {
        // given
        val keywords = MovieKeywordsTestData.Inception
        val movieId = keywords.movieId
        every { localMovieDataSource.findMovieKeywords(movieId) } returns flowOf(keywords.right())
        coEvery { remoteMovieDataSource.getMovieKeywords(movieId) } returns keywords.right()

        // when
        repository.getMovieKeywords(movieId).test {

            // then
            assertEquals(keywords.right(), awaitItem())
            cancelAndConsumeRemainingEvents()
            coVerifySequence {
                localMovieDataSource.findMovieKeywords(movieId)
                remoteMovieDataSource.getMovieKeywords(movieId)
                localMovieDataSource.insertKeywords(keywords)
            }
        }
    }

    @Test
    fun `get suggested movies calls local data source`() = runTest {
        // given
        val movies = nonEmptyListOf(MovieTestData.Inception, MovieTestData.TheWolfOfWallStreet).right()
        every { localMovieDataSource.findAllSuggestedMovies() } returns flowOf(movies)

        // when
        repository.getSuggestedMovies().test {

            // then
            assertEquals(movies, awaitItem())
            cancelAndConsumeRemainingEvents()
            verify { localMovieDataSource.findAllSuggestedMovies() }
        }
    }

    @Test
    fun `rate movie inserts locally and post to remote`() = runTest {
        // given
        val movieId = MovieTestData.Inception.tmdbId
        Rating.of(8).tap { rating ->

            // when
            val result = repository.rate(movieId, rating)

            // then
            assertEquals(Unit.right(), result)
            coVerifySequence {
                localMovieDataSource.insertRating(movieId, rating)
                remoteMovieDataSource.postRating(movieId, rating)
            }
        }
    }

    @Test
    fun `store suggested movies inserts locally`() = runTest {
        // given
        val movies = listOf(MovieTestData.Inception, MovieTestData.TheWolfOfWallStreet)

        // when
        repository.storeSuggestedMovies(movies)

        // then
        coVerify { localMovieDataSource.insertSuggestedMovies(movies) }
    }
}
