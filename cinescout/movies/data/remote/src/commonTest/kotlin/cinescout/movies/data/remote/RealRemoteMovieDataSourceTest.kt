package cinescout.movies.data.remote

import arrow.core.left
import arrow.core.right
import cinescout.auth.tmdb.domain.usecase.IsTmdbLinked
import cinescout.auth.trakt.domain.usecase.IsTraktLinked
import cinescout.error.NetworkError
import cinescout.movies.data.remote.testdata.TraktMovieRatingTestData
import cinescout.movies.domain.model.Rating
import cinescout.movies.domain.testdata.DiscoverMoviesParamsTestData
import cinescout.movies.domain.testdata.MovieCreditsTestData
import cinescout.movies.domain.testdata.MovieKeywordsTestData
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithDetailsTestData
import cinescout.movies.domain.testdata.MovieWithPersonalRatingTestData
import cinescout.movies.domain.testdata.TmdbMovieIdTestData
import cinescout.network.DualSourceCall
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import store.PagedData
import store.Paging
import store.builder.pagedDataOf
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RealRemoteMovieDataSourceTest {

    private val isTmdbLinked: IsTmdbLinked = mockk {
        coEvery { this@mockk() } returns true
    }
    private val isTraktLinked: IsTraktLinked = mockk {
        coEvery { this@mockk() } returns true
    }
    private val dualSourceCall = DualSourceCall(isFirstSourceLinked = { true }, isSecondSourceLinked = { true })
    private val tmdbSource: TmdbRemoteMovieDataSource = mockk(relaxUnitFun = true) {
        coEvery { postRating(any(), any()) } returns Unit.right()
        coEvery { postAddToWatchlist(id = any()) } returns Unit.right()
    }
    private val traktSource: TraktRemoteMovieDataSource = mockk(relaxUnitFun = true) {
        coEvery { postRating(any(), any()) } returns Unit.right()
        coEvery { postWatchlist(id = any()) } returns Unit.right()

    }
    private val remoteMovieDataSource = RealRemoteMovieDataSource(
        dualSourceCall = dualSourceCall,
        isTmdbLinked = isTmdbLinked,
        isTraktLinked = isTraktLinked,
        tmdbSource = tmdbSource,
        traktSource = traktSource
    )

    @Test
    fun `discover movies returns the right movies from Tmdb`() = runTest {
        // given
        val expected = listOf(MovieTestData.Inception, MovieTestData.TheWolfOfWallStreet).right()
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
        val movieId = TmdbMovieIdTestData.Inception
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
        val movieId = TmdbMovieIdTestData.Inception
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
        val movieId = TmdbMovieIdTestData.Inception
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
                MovieWithPersonalRatingTestData.Inception,
                MovieWithPersonalRatingTestData.TheWolfOfWallStreet,
                MovieWithPersonalRatingTestData.War
            ),
            paging = Paging.Page.DualSources.Initial
        ).right()
        coEvery { tmdbSource.getMovieDetails(TmdbMovieIdTestData.TheWolfOfWallStreet) } returns
            MovieWithDetailsTestData.TheWolfOfWallStreet.right()
        coEvery { tmdbSource.getMovieDetails(TmdbMovieIdTestData.War) } returns
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
        assertEquals(
            expected,
            result,
            message =
            """Expected ${expected.orNull()!!.data.map { it.movie.title } }
               but was  ${result.orNull()!!.data.map { it.movie.title } }
            """.trimIndent()
        )
    }

    @Test
    fun `get rated movies return the right ratings from Tmdb if Trakt not linked`() = runTest {
        // given
        val expected = PagedData.Remote(
            data = listOf(
                MovieWithPersonalRatingTestData.Inception,
                MovieWithPersonalRatingTestData.TheWolfOfWallStreet
            ),
            paging = Paging.Page.DualSources.Initial
        ).right()
        coEvery { isTraktLinked() } returns false
        coEvery { tmdbSource.getMovieDetails(TmdbMovieIdTestData.TheWolfOfWallStreet) } returns
            MovieWithDetailsTestData.TheWolfOfWallStreet.right()
        coEvery { tmdbSource.getMovieDetails(TmdbMovieIdTestData.War) } returns
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
        assertEquals(
            expected,
            result,
            message =
            """Expected ${expected.orNull()!!.data.map { it.movie.title } }
               but was  ${result.orNull()!!.data.map { it.movie.title } }
            """.trimIndent()
        )
    }

    @Test
    fun `get rated movies return the right ratings from Trakt if Tmdb not linked`() = runTest {
        // given
        val expected = PagedData.Remote(
            data = listOf(
                MovieWithPersonalRatingTestData.TheWolfOfWallStreet,
                MovieWithPersonalRatingTestData.War
            ),
            paging = Paging.Page.DualSources.Initial
        ).right()
        coEvery { isTmdbLinked() } returns false
        coEvery { tmdbSource.getMovieDetails(TmdbMovieIdTestData.TheWolfOfWallStreet) } returns
            MovieWithDetailsTestData.TheWolfOfWallStreet.right()
        coEvery { tmdbSource.getMovieDetails(TmdbMovieIdTestData.War) } returns
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
        assertEquals(
            expected,
            result,
            message =
            """Expected ${expected.orNull()!!.data.map { it.movie.title } }
               but was  ${result.orNull()!!.data.map { it.movie.title } }
            """.trimIndent()
        )
    }

    @Test
    fun `get rated movies return error is Tmdb and Trakt are not linked`() = runTest {
        // given
        val expected = NetworkError.Unauthorized.left()
        coEvery { isTmdbLinked() } returns false
        coEvery { isTraktLinked() } returns false
        coEvery { tmdbSource.getMovieDetails(TmdbMovieIdTestData.TheWolfOfWallStreet) } returns
            MovieWithDetailsTestData.TheWolfOfWallStreet.right()
        coEvery { tmdbSource.getMovieDetails(TmdbMovieIdTestData.War) } returns
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
    fun `post rating posts to tmdb and trakt`() = runTest {
        // given
        val movieId = MovieTestData.Inception.tmdbId

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
        val movieId = MovieTestData.Inception.tmdbId

        // when
        val result = remoteMovieDataSource.postAddToWatchlist(movieId)

        // then
        assertEquals(Unit.right(), result)
        coVerify { tmdbSource.postAddToWatchlist(movieId) }
        coVerify { traktSource.postWatchlist(movieId) }
    }
}
