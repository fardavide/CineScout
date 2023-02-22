package cinescout.movies.data

import app.cash.turbine.test
import arrow.core.nonEmptyListOf
import arrow.core.right
import cinescout.common.model.Rating
import cinescout.movies.domain.model.MovieIdWithPersonalRating
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.sample.MovieWithDetailsSample
import cinescout.movies.domain.sample.MovieWithPersonalRatingSample
import cinescout.movies.domain.sample.TmdbMovieIdSample
import cinescout.movies.domain.testdata.DiscoverMoviesParamsTestData
import cinescout.movies.domain.testdata.MovieCreditsTestData
import cinescout.movies.domain.testdata.MovieKeywordsTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import store.Paging
import store.Refresh
import store.builder.toPagedData
import store.test.MockStoreOwner
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RealMovieRepositoryTest {

    private val dispatcher = StandardTestDispatcher()
    private val localMovieDataSource: LocalMovieDataSource = mockk(relaxUnitFun = true) {
        every { findMovieWithDetails(TmdbMovieIdSample.Inception) } returns
            flowOf(MovieWithDetailsSample.Inception)
        every { findMovieWithDetails(TmdbMovieIdSample.TheWolfOfWallStreet) } returns
            flowOf(MovieWithDetailsSample.TheWolfOfWallStreet)
    }
    private val remoteMovieDataSource: RemoteMovieDataSource = mockk(relaxUnitFun = true) {
        coEvery { discoverMovies(any()) } returns
            listOf(MovieSample.Inception, MovieSample.TheWolfOfWallStreet).right()
        coEvery { getMovieDetails(TmdbMovieIdSample.Inception) } returns
            MovieWithDetailsSample.Inception.right()
        coEvery { getMovieDetails(TmdbMovieIdSample.TheWolfOfWallStreet) } returns
            MovieWithDetailsSample.TheWolfOfWallStreet.right()
        coEvery { postAddToWatchlist(any()) } returns Unit.right()
        coEvery { postRating(any(), any()) } returns Unit.right()
    }
    private val storeOwner = MockStoreOwner()
    private val repository = RealMovieRepository(
        localMovieDataSource = localMovieDataSource,
        remoteMovieDataSource = remoteMovieDataSource,
        storeOwner = storeOwner
    )

    @Test
    fun `add to disliked inserts locally and post to remote`() = runTest(dispatcher) {
        // given
        val movieId = MovieSample.Inception.tmdbId

        // when
        repository.addToDisliked(movieId)

        // then
        coVerifySequence {
            localMovieDataSource.insertDisliked(movieId)
        }
    }

    @Test
    fun `add to liked inserts locally and post to remote`() = runTest(dispatcher) {
        // given
        val movieId = MovieSample.Inception.tmdbId

        // when
        repository.addToLiked(movieId)

        // then
        coVerifySequence {
            localMovieDataSource.insertLiked(movieId)
        }
    }

    @Test
    fun `add to watchlist inserts locally and post to remote`() = runTest(dispatcher) {
        // given
        val movieId = MovieSample.Inception.tmdbId

        // when
        repository.addToWatchlist(movieId)

        // then
        coVerifySequence {
            localMovieDataSource.insertWatchlist(movieId)
            remoteMovieDataSource.postAddToWatchlist(movieId)
        }
    }

    @Test
    fun `add movie to watchlist inserts locally and post to remote`() = runTest(dispatcher) {
        // given
        val movieId = MovieSample.Inception.tmdbId

        // when
        repository.addToWatchlist(movieId)

        // then
        coVerifySequence {
            localMovieDataSource.insertWatchlist(movieId)
            remoteMovieDataSource.postAddToWatchlist(movieId)
        }
    }

    @Test
    fun `discover movies calls local and remote data sources`() = runTest(dispatcher) {
        // given
        val movies = listOf(MovieSample.Inception, MovieSample.TheWolfOfWallStreet)
        val params = DiscoverMoviesParamsTestData.FromInception

        // when
        repository.discoverMovies(params).test {

            // then
            assertEquals(movies.right(), awaitItem())
            coVerifySequence {
                remoteMovieDataSource.discoverMovies(params)
                localMovieDataSource.insert(movies)
            }
            awaitComplete()
        }
    }

    @Test
    fun `get all disliked movies calls local data sources`() = runTest(dispatcher) {
        // given
        val movies = listOf(MovieSample.Inception, MovieSample.TheWolfOfWallStreet)
        every { localMovieDataSource.findAllDislikedMovies() } returns flowOf(movies)

        // when
        repository.getAllDislikedMovies().test {

            // then
            assertEquals(movies, awaitItem())
            cancelAndIgnoreRemainingEvents()
            coVerifySequence {
                localMovieDataSource.findAllDislikedMovies()
            }
        }
    }

    @Test
    fun `get all liked movies calls local data sources`() = runTest(dispatcher) {
        // given
        val movies = listOf(MovieSample.Inception, MovieSample.TheWolfOfWallStreet)
        every { localMovieDataSource.findAllLikedMovies() } returns flowOf(movies)

        // when
        repository.getAllLikedMovies().test {

            // then
            assertEquals(movies, awaitItem())
            cancelAndIgnoreRemainingEvents()
            coVerifySequence {
                localMovieDataSource.findAllLikedMovies()
            }
        }
    }

    @Test
    fun `get all rated movies calls local and remote data sources`() = runTest(dispatcher) {
        // given
        val movies = listOf(
            MovieWithPersonalRatingSample.Inception,
            MovieWithPersonalRatingSample.TheWolfOfWallStreet
        )
        val moviesWithDetails = listOf(
            MovieWithDetailsSample.Inception,
            MovieWithDetailsSample.TheWolfOfWallStreet
        )
        val pagedMovies = movies.toPagedData(Paging.Page.DualSources.Initial).map { movieWithPersonalRating ->
            MovieIdWithPersonalRating(
                movieWithPersonalRating.movie.tmdbId,
                movieWithPersonalRating.personalRating
            )
        }
        every { localMovieDataSource.findAllRatedMovies() } returns flowOf(movies)
        coEvery { remoteMovieDataSource.getRatedMovies(any()) } returns pagedMovies.right()

        // when
        repository.getAllRatedMovies(Refresh.IfNeeded).test {

            // then
            assertEquals(movies.right(), awaitItem().map { it.data })
            coVerifySequence {
                localMovieDataSource.findAllRatedMovies()
                remoteMovieDataSource.getRatedMovies(any())
                for (movie in moviesWithDetails) {
                    localMovieDataSource.findMovieWithDetails(movie.movie.tmdbId)
                    remoteMovieDataSource.getMovieDetails(movie.movie.tmdbId)
                    localMovieDataSource.insert(movie)
                }
                localMovieDataSource.insertRatings(movies)
                localMovieDataSource.findAllRatedMovies()
            }
        }
    }

    @Test
    fun `get all watchlist movies calls local and remote data sources`() = runTest(dispatcher) {
        // given
        val moviesWithDetails = listOf(
            MovieWithDetailsSample.Inception,
            MovieWithDetailsSample.TheWolfOfWallStreet
        )
        val movies = moviesWithDetails.map { it.movie }
        val pagedMoviesIds = movies.map { it.tmdbId }.toPagedData(Paging.Page.DualSources.Initial)
        every { localMovieDataSource.findAllWatchlistMovies() } returns flowOf(movies)
        coEvery { remoteMovieDataSource.getWatchlistMovies(any()) } returns pagedMoviesIds.right()

        // when
        repository.getAllWatchlistMovies(Refresh.IfNeeded).test {

            // then
            assertEquals(movies.right(), awaitItem().map { it.data })
            coVerifySequence {
                localMovieDataSource.findAllWatchlistMovies()
                remoteMovieDataSource.getWatchlistMovies(any())
                for (movieWithDetails in moviesWithDetails) {
                    localMovieDataSource.findMovieWithDetails(movieWithDetails.movie.tmdbId)
                    remoteMovieDataSource.getMovieDetails(movieWithDetails.movie.tmdbId)
                    localMovieDataSource.insert(movieWithDetails)
                }
                localMovieDataSource.insertWatchlist(movies)
                localMovieDataSource.findAllWatchlistMovies()
                localMovieDataSource.deleteWatchlist(movies = any())
            }
        }
    }

    @Test
    fun `get movie calls local and remote data sources`() = runTest(dispatcher) {
        // given
        val movie = MovieWithDetailsSample.Inception
        val movieId = movie.movie.tmdbId
        every { localMovieDataSource.findMovieWithDetails(movieId) } returns flowOf(movie)
        coEvery { remoteMovieDataSource.getMovieDetails(movieId) } returns movie.right()

        // when
        repository.getMovieDetails(movieId, Refresh.IfNeeded).test {

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
    fun `get movie credits calls local and remote data sources`() = runTest(dispatcher) {
        // given
        val credits = MovieCreditsTestData.Inception
        val movieId = credits.movieId
        every { localMovieDataSource.findMovieCredits(movieId) } returns flowOf(credits)
        coEvery { remoteMovieDataSource.getMovieCredits(movieId) } returns credits.right()

        // when
        repository.getMovieCredits(movieId, Refresh.IfNeeded).test {

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
    fun `get movie keywords calls local and remote data sources`() = runTest(dispatcher) {
        // given
        val keywords = MovieKeywordsTestData.Inception
        val movieId = keywords.movieId
        every { localMovieDataSource.findMovieKeywords(movieId) } returns flowOf(keywords)
        coEvery { remoteMovieDataSource.getMovieKeywords(movieId) } returns keywords.right()

        // when
        repository.getMovieKeywords(movieId, Refresh.IfNeeded).test {

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
    fun `get suggested movies calls local data source`() = runTest(dispatcher) {
        // given
        val movies = nonEmptyListOf(MovieSample.Inception, MovieSample.TheWolfOfWallStreet).right()
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
    fun `rate movie inserts locally and post to remote`() = runTest(dispatcher) {
        // given
        val movieId = MovieSample.Inception.tmdbId
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
    fun `store suggested movies inserts locally`() = runTest(dispatcher) {
        // given
        val movies = listOf(MovieSample.Inception, MovieSample.TheWolfOfWallStreet)

        // when
        repository.storeSuggestedMovies(movies)

        // then
        coVerify { localMovieDataSource.insertSuggestedMovies(movies) }
    }
}
