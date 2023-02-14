package cinescout.movies.data.remote

import arrow.core.left
import arrow.core.right
import cinescout.common.model.Rating
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.movies.data.remote.testdata.TraktMovieRatingTestData
import cinescout.movies.domain.sample.MovieSample
import cinescout.movies.domain.sample.TmdbMovieIdSample
import cinescout.movies.domain.testdata.DiscoverMoviesParamsTestData
import cinescout.movies.domain.testdata.MovieCreditsTestData
import cinescout.movies.domain.testdata.MovieIdWithPersonalRatingTestData
import cinescout.movies.domain.testdata.MovieKeywordsTestData
import cinescout.movies.domain.testdata.MovieWithDetailsTestData
import cinescout.movies.domain.testdata.MovieWithPersonalRatingTestData
import cinescout.test.kotlin.TestTimeoutMs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import store.PagedData
import store.Paging
import store.builder.dualSourcesPagedDataOf
import store.builder.pagedDataOf
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RealRemoteMovieDataSourceTest {

    private val tmdbSource: TmdbRemoteMovieDataSource = mockk(relaxUnitFun = true) {
        coEvery { getMovieDetails(TmdbMovieIdSample.TheWolfOfWallStreet) } returns
            MovieWithDetailsTestData.TheWolfOfWallStreet.right()
        coEvery { getMovieDetails(TmdbMovieIdSample.War) } returns
            MovieWithDetailsTestData.War.right()
        coEvery { postRating(any(), any()) } returns Unit.right()
        coEvery { postAddToWatchlist(movieId = any()) } returns Unit.right()
    }
    private val traktSource: TraktRemoteMovieDataSource = mockk(relaxUnitFun = true) {
        coEvery { postRating(any(), any()) } returns Unit.right()
        coEvery { postAddToWatchlist(movieId = any()) } returns Unit.right()
    }
    private val remoteMovieDataSource = RealRemoteMovieDataSource(
        tmdbSource = tmdbSource,
        traktSource = traktSource
    )

    @Test
    fun `discover movies returns the right movies from Tmdb`() = runTest {
        // given
        val expected = listOf(MovieSample.Inception, MovieSample.TheWolfOfWallStreet).right()
        val params = DiscoverMoviesParamsTestData.FromInception
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
        val expected = MovieWithDetailsTestData.Inception.right()
        val movieId = TmdbMovieIdSample.Inception
        coEvery { tmdbSource.getMovieDetails(movieId) } returns expected

        // when
        val result = remoteMovieDataSource.getMovieDetails(movieId)

        // then
        assertEquals(expected, result)
        coVerify { tmdbSource.getMovieDetails(movieId) }
    }

    @Test
    fun `get movie credits returns the right credits from Tmdb`() = runTest {
        // given
        val expected = MovieCreditsTestData.Inception.right()
        val movieId = TmdbMovieIdSample.Inception
        coEvery { tmdbSource.getMovieCredits(movieId) } returns expected

        // when
        val result = remoteMovieDataSource.getMovieCredits(movieId)

        // then
        assertEquals(expected, result)
        coVerify { tmdbSource.getMovieCredits(movieId) }
    }

    @Test
    fun `get movie keywords returns the right keywords from Tmdb`() = runTest {
        // given
        val expected = MovieKeywordsTestData.Inception.right()
        val movieId = TmdbMovieIdSample.Inception
        coEvery { tmdbSource.getMovieKeywords(movieId) } returns expected

        // when
        val result = remoteMovieDataSource.getMovieKeywords(movieId)

        // then
        assertEquals(expected, result)
        coVerify { tmdbSource.getMovieKeywords(movieId) }
    }

    @Test
    fun `get rated movies return the right ratings from Tmdb and Trakt`() = runTest {
        // given
        val expected = PagedData.Remote(
            data = listOf(
                MovieIdWithPersonalRatingTestData.Inception,
                MovieIdWithPersonalRatingTestData.TheWolfOfWallStreet,
                MovieIdWithPersonalRatingTestData.War
            ),
            paging = Paging.Page.DualSources.Initial
        ).right()
        coEvery { tmdbSource.getMovieDetails(TmdbMovieIdSample.TheWolfOfWallStreet) } returns
            MovieWithDetailsTestData.TheWolfOfWallStreet.right()
        coEvery { tmdbSource.getMovieDetails(TmdbMovieIdSample.War) } returns
            MovieWithDetailsTestData.War.right()
        coEvery { tmdbSource.getRatedMovies(1) } returns
            pagedDataOf(
                MovieWithPersonalRatingTestData.Inception,
                MovieWithPersonalRatingTestData.TheWolfOfWallStreet
            ).right()
        coEvery { traktSource.getRatedMovies(1) } returns
            pagedDataOf(TraktMovieRatingTestData.TheWolfOfWallStreet, TraktMovieRatingTestData.War).right()

        // when
        val result = remoteMovieDataSource.getRatedMovies(Paging.Page.DualSources.Initial)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get rated movies return the right ratings from Tmdb if Trakt not linked`() = runTest {
        // given
        val expected = PagedData.Remote(
            data = listOf(
                MovieIdWithPersonalRatingTestData.Inception,
                MovieIdWithPersonalRatingTestData.TheWolfOfWallStreet
            ),
            paging = Paging.Page.DualSources.Initial
        ).right()
        coEvery { tmdbSource.getRatedMovies(page = any()) } returns
            pagedDataOf(
                MovieWithPersonalRatingTestData.Inception,
                MovieWithPersonalRatingTestData.TheWolfOfWallStreet
            ).right()
        coEvery { traktSource.getRatedMovies(page = any()) } returns NetworkOperation.Skipped.left()

        // when
        val result = remoteMovieDataSource.getRatedMovies(Paging.Page.DualSources.Initial)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get rated movies return the right ratings from Trakt if Tmdb not linked`() = runTest {
        // given
        val expected = PagedData.Remote(
            data = listOf(
                MovieIdWithPersonalRatingTestData.TheWolfOfWallStreet,
                MovieIdWithPersonalRatingTestData.War
            ),
            paging = Paging.Page.DualSources.Initial
        ).right()
        coEvery { tmdbSource.getRatedMovies(1) } returns NetworkOperation.Skipped.left()
        coEvery { traktSource.getRatedMovies(1) } returns
            pagedDataOf(TraktMovieRatingTestData.TheWolfOfWallStreet, TraktMovieRatingTestData.War).right()

        // when
        val result = remoteMovieDataSource.getRatedMovies(Paging.Page.DualSources.Initial)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get rated movies delivers unauthorized error if linked to Tmdb`() = runTest {
        // given
        val expected = NetworkOperation.Error(NetworkError.Unauthorized).left()
        coEvery { tmdbSource.getRatedMovies(page = any()) } returns expected
        coEvery { traktSource.getRatedMovies(page = any()) } returns expected

        // when
        val result = remoteMovieDataSource.getRatedMovies(Paging.Page.DualSources.Initial)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get rated movies delivers unauthorized error if linked to Trakt`() = runTest {
        // given
        val expected = NetworkOperation.Error(NetworkError.Unauthorized).left()
        coEvery { tmdbSource.getRatedMovies(page = any()) } returns expected
        coEvery { traktSource.getRatedMovies(page = any()) } returns expected

        // when
        val result = remoteMovieDataSource.getRatedMovies(Paging.Page.DualSources.Initial)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get watchlist delivers data when only Tmdb is linked`() = runTest(
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        val movie = MovieSample.Inception
        val expected = dualSourcesPagedDataOf(MovieSample.Inception.tmdbId).right()
        coEvery { tmdbSource.getWatchlistMovies(page = any()) } returns pagedDataOf(movie).right()
        coEvery { traktSource.getWatchlistMovies(page = any()) } returns NetworkOperation.Skipped.left()

        // when
        val result = remoteMovieDataSource.getWatchlistMovies(Paging.Page.DualSources.Initial)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get watchlist delivers data when only Trakt is linked`() = runTest(
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        val movie = MovieSample.Inception
        val expected = dualSourcesPagedDataOf(MovieSample.Inception.tmdbId).right()
        coEvery { tmdbSource.getWatchlistMovies(page = any()) } returns NetworkOperation.Skipped.left()
        coEvery { traktSource.getWatchlistMovies(page = any()) } returns pagedDataOf(movie.tmdbId).right()

        // when
        val result = remoteMovieDataSource.getWatchlistMovies(Paging.Page.DualSources.Initial)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get watchlist skips when none of Tmdb and Trakt are linked`() = runTest(
        dispatchTimeoutMs = TestTimeoutMs
    ) {
        // given
        val expected = NetworkOperation.Skipped.left()
        coEvery { tmdbSource.getWatchlistMovies(page = any()) } returns NetworkOperation.Skipped.left()
        coEvery { traktSource.getWatchlistMovies(page = any()) } returns NetworkOperation.Skipped.left()

        // when
        val result = remoteMovieDataSource.getWatchlistMovies(Paging.Page.DualSources.Initial)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `post rating posts to tmdb and trakt`() = runTest {
        // given
        val movieId = MovieSample.Inception.tmdbId

        // when
        Rating.of(8).tap { rating ->
            val result = remoteMovieDataSource.postRating(movieId, rating)

            // then
            assertEquals(Unit.right(), result)
            coVerify { tmdbSource.postRating(movieId, rating) }
            coVerify { traktSource.postRating(movieId, rating) }
        }
    }

    @Test
    fun `post watchlist posts to tmdb and trakt`() = runTest {
        // given
        val movieId = MovieSample.Inception.tmdbId

        // when
        val result = remoteMovieDataSource.postAddToWatchlist(movieId)

        // then
        assertEquals(Unit.right(), result)
        coVerify { tmdbSource.postAddToWatchlist(movieId) }
        coVerify { traktSource.postAddToWatchlist(movieId) }
    }
}
