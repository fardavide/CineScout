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
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.movies.domain.testdata.MovieWithRatingTestData
import cinescout.movies.domain.testdata.TmdbMovieIdTestData
import cinescout.network.DualSourceCall
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

    private val isTmdbLinked: IsTmdbLinked = mockk {
        coEvery { this@mockk() } returns true
    }
    private val isTraktLinked: IsTraktLinked = mockk {
        coEvery { this@mockk() } returns true
    }
    private val dualSourceCall = DualSourceCall(isFirstSourceLinked = { true }, isSecondSourceLinked = { true })
    private val tmdbSource: TmdbRemoteMovieDataSource = mockk(relaxUnitFun = true) {
        coEvery { postDisliked(id = any()) } returns Unit.right()
        coEvery { postLiked(id = any()) } returns Unit.right()
        coEvery { postRating(any(), any()) } returns Unit.right()
        coEvery { postAddToWatchlist(id = any()) } returns Unit.right()
    }
    private val traktSource: TraktRemoteMovieDataSource = mockk(relaxUnitFun = true) {
        coEvery { postDisliked(id = any()) } returns Unit.right()
        coEvery { postLiked(id = any()) } returns Unit.right()
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
    fun `get rated movies return the right ratings from Tmdb and Trakt`() = runTest {
        // given
        val expected = PagedData.Remote(
            data = listOf(
                MovieWithRatingTestData.Inception,
                MovieWithRatingTestData.TheWolfOfWallStreet,
                MovieWithRatingTestData.War
            ),
            paging = Paging.Page.DualSources.Initial
        ).right()
        coEvery { tmdbSource.getMovie(TmdbMovieIdTestData.TheWolfOfWallStreet) } returns
            MovieTestData.TheWolfOfWallStreet.right()
        coEvery { tmdbSource.getMovie(TmdbMovieIdTestData.War) } returns
            MovieTestData.War.right()
        coEvery { tmdbSource.getRatedMovies(1) } returns
            pagedDataOf(MovieWithRatingTestData.Inception, MovieWithRatingTestData.TheWolfOfWallStreet).right()
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
                MovieWithRatingTestData.Inception,
                MovieWithRatingTestData.TheWolfOfWallStreet
            ),
            paging = Paging.Page.DualSources.Initial
        ).right()
        coEvery { isTraktLinked() } returns false
        coEvery { tmdbSource.getMovie(TmdbMovieIdTestData.TheWolfOfWallStreet) } returns
            MovieTestData.TheWolfOfWallStreet.right()
        coEvery { tmdbSource.getMovie(TmdbMovieIdTestData.War) } returns
            MovieTestData.War.right()
        coEvery { tmdbSource.getRatedMovies(1) } returns
            pagedDataOf(MovieWithRatingTestData.Inception, MovieWithRatingTestData.TheWolfOfWallStreet).right()
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
                MovieWithRatingTestData.TheWolfOfWallStreet,
                MovieWithRatingTestData.War
            ),
            paging = Paging.Page.DualSources.Initial
        ).right()
        coEvery { isTmdbLinked() } returns false
        coEvery { tmdbSource.getMovie(TmdbMovieIdTestData.TheWolfOfWallStreet) } returns
            MovieTestData.TheWolfOfWallStreet.right()
        coEvery { tmdbSource.getMovie(TmdbMovieIdTestData.War) } returns
            MovieTestData.War.right()
        coEvery { tmdbSource.getRatedMovies(1) } returns
            pagedDataOf(MovieWithRatingTestData.Inception, MovieWithRatingTestData.TheWolfOfWallStreet).right()
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
        coEvery { tmdbSource.getMovie(TmdbMovieIdTestData.TheWolfOfWallStreet) } returns
            MovieTestData.TheWolfOfWallStreet.right()
        coEvery { tmdbSource.getMovie(TmdbMovieIdTestData.War) } returns
            MovieTestData.War.right()
        coEvery { tmdbSource.getRatedMovies(1) } returns
            pagedDataOf(MovieWithRatingTestData.Inception, MovieWithRatingTestData.TheWolfOfWallStreet).right()
        coEvery { traktSource.getRatedMovies(1) } returns
            pagedDataOf(TraktMovieRatingTestData.TheWolfOfWallStreet, TraktMovieRatingTestData.War).right()

        // when
        val result = remoteMovieDataSource.getRatedMovies(Paging.Page.DualSources.Initial)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `post disliked posts to tmdb and trakt`() = runTest {
        // given
        val movieId = MovieTestData.Inception.tmdbId

        // when
        val result = remoteMovieDataSource.postDisliked(movieId)

        // then
        assertEquals(Unit.right(), result)
        coVerify { tmdbSource.postDisliked(movieId) }
        coVerify { traktSource.postDisliked(movieId) }
    }

    @Test
    fun `post liked posts to tmdb and trakt`() = runTest {
        // given
        val movieId = MovieTestData.Inception.tmdbId

        // when
        val result = remoteMovieDataSource.postLiked(movieId)

        // then
        assertEquals(Unit.right(), result)
        coVerify { tmdbSource.postLiked(movieId) }
        coVerify { traktSource.postLiked(movieId) }
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
