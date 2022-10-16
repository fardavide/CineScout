package cinescout.movies.data.remote.trakt

import arrow.core.Either
import arrow.core.right
import cinescout.auth.trakt.domain.usecase.CallWithTraktAccount
import cinescout.common.model.Rating
import cinescout.error.NetworkError
import cinescout.model.NetworkOperation
import cinescout.movies.data.remote.testdata.TraktMovieRatingTestData
import cinescout.movies.data.remote.trakt.mapper.TraktMovieMapper
import cinescout.movies.data.remote.trakt.service.TraktMovieService
import cinescout.movies.data.remote.trakt.testdata.GetRatingsTestData
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.testdata.MovieTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import store.PagedData
import store.Paging
import store.builder.pagedDataOf
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RealTraktMovieDataSourceTest {

    private val callWithTraktAccount: CallWithTraktAccount = mockk {
        coEvery { invoke<PagedData.Remote<TmdbMovieId, Paging.Page.SingleSource>>(block = any()) } answers {
            firstArg<() -> Either<NetworkError, PagedData.Remote<TmdbMovieId, Paging.Page.SingleSource>>>().invoke()
                .mapLeft { NetworkOperation.Error(it) }
        }
    }
    private val movieMapper: TraktMovieMapper = mockk {
        every { toMovieRating(any()) } returns TraktMovieRatingTestData.Inception
    }
    private val service: TraktMovieService = mockk {
        coEvery { getRatedMovies(any()) } returns pagedDataOf(GetRatingsTestData.Inception).right()
        coEvery { postAddToWatchlist(any()) } returns Unit.right()
        coEvery { postRating(any(), any()) } returns Unit.right()
    }
    private val dataSource = RealTraktMovieDataSource(
        callWithTraktAccount = callWithTraktAccount,
        movieMapper = movieMapper,
        service = service
    )

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
        val expected = pagedDataOf(TraktMovieRatingTestData.Inception).right()

        // when
        val result = dataSource.getRatedMovies(1)

        // then
        assertEquals(expected, result)
    }


    @Test
    fun `post watchlist does call service`() = runTest {
        // given
        val expected = Unit.right()
        val movieId = MovieTestData.Inception.tmdbId

        // when
        val result = dataSource.postAddToWatchlist(movieId)

        // then
        coVerify { service.postAddToWatchlist(movieId) }
        assertEquals(expected, result)
    }

    @Test
    fun `post rating does call service`() = runTest {
        // given
        val expected = Unit.right()
        val movieId = MovieTestData.Inception.tmdbId
        Rating.of(8).tap { rating ->

            // when
            val result = dataSource.postRating(movieId, rating)

            // then
            coVerify { service.postRating(movieId, rating) }
            assertEquals(expected, result)
        }
    }
}
