package client.viewModel

import client.util.ViewModelTest
import kotlin.test.*
import assert4k.*
import client.ViewState
import client.double.StubFindMovie
import client.double.StubGetMovieRating
import client.double.StubIsMovieInWatchlist
import domain.AddMovieToWatchlist
import domain.RateMovie
import domain.RemoveMovieFromWatchlist
import domain.Test.Movie.TheBookOfEli
import entities.UserRating
import entities.movies.MovieWithStats
import io.mockk.coEvery
import io.mockk.coVerifyAll
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope

class MovieDetailsViewModelTest : ViewModelTest {

    private val mockRateMovie = mockk<RateMovie>(relaxed = true)
    private val mockAddMovieToWatchlist = mockk<AddMovieToWatchlist>(relaxed = true)
    private val mockRemoveMovieFromWatchlist = mockk<RemoveMovieFromWatchlist>(relaxed = true)

    private fun CoroutineScope.ViewModel(
        rateMovie: RateMovie = mockRateMovie
    ) = MovieDetailsViewModel(
        scope = this,
        dispatchers = dispatchers,
        movieId = TheBookOfEli.id,
        findMovie = StubFindMovie(TheBookOfEli),
        getMovieRating = StubGetMovieRating(UserRating.Positive),
        isMovieInWatchlist = StubIsMovieInWatchlist(true),
        addMovieToWatchlist = mockAddMovieToWatchlist,
        removeMovieFromWatchlist = mockRemoveMovieFromWatchlist,
        rateMovie = rateMovie,
    )

    @Test
    fun `show data correctly`() = viewModelTest({
        ViewModel()
    }) { viewModel ->
        assert that viewModel.result *{
            +state `is` type<ViewState.Success<MovieWithStats>>()
            +data equals MovieWithStats(TheBookOfEli, UserRating.Positive, true)
        }
    }

    @Test
    fun `show data again after error`() = viewModelTest({

        ViewModel(
            rateMovie = mockk {
                coEvery { this@mockk.invoke(any(), any()) } answers { throw Exception() }
            }
        )

    }) { viewModel ->

        assert that viewModel.result *{
            +state `is` type<ViewState.Success<MovieWithStats>>()
            +data equals MovieWithStats(TheBookOfEli, UserRating.Positive, true)
        }

        viewModel.dislike()
        assert that viewModel.result.state `is` type<MovieDetailsViewModel.Error.CantRate>()

        advanceTimeBy(CineViewModel.ErrorDelay.toLongMilliseconds())
        assert that viewModel.result *{
            +state `is` type<ViewState.Success<MovieWithStats>>()
            +data equals MovieWithStats(TheBookOfEli, UserRating.Positive, true)
        }

    }

    @Test
    fun `can rate`() = viewModelTest({
        ViewModel()
    }) { viewModel ->
        viewModel.dislike()
        viewModel.like()

        coVerifyAll {
            mockRateMovie(TheBookOfEli, UserRating.Negative)
            mockRateMovie(TheBookOfEli, UserRating.Positive)
        }
    }

    @Test
    fun `can add and remove to watchlist`() = viewModelTest({
        ViewModel()
    }) { viewModel ->
        viewModel.addToWatchlist()
        viewModel.removeFromWatchlist()

        coVerifyAll {
            mockAddMovieToWatchlist(TheBookOfEli)
            mockRemoveMovieFromWatchlist(TheBookOfEli)
        }
    }
}