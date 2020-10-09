package client.viewModel

import assert4k.*
import client.ViewState.Error
import client.ViewState.Loading
import client.nextData
import client.util.ViewModelTest
import domain.stats.AddMovieToWatchlist
import domain.FindMovie
import domain.MockMovieRepository
import domain.MockStatRepository
import domain.stats.RateMovie
import domain.Test.Movie.Fury
import domain.Test.Movie.Inception
import entities.UserRating
import entities.UserRating.Positive
import entities.movies.Movie
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlin.test.*

internal class RateMovieViewModelTest : ViewModelTest {

    private val stats = MockStatRepository()
    private val mockAddMovieToWatchlist = mockk<AddMovieToWatchlist>(relaxed = true)

    private fun CoroutineScope.ViewModel(
        addMovieToWatchlist: AddMovieToWatchlist = AddMovieToWatchlist(stats),
        rateMovie: RateMovie = RateMovie(stats)
    ): RateMovieViewModel {
        return RateMovieViewModel(
            this,
            dispatchers,
            addMovieToWatchlist,
            rateMovie,
            FindMovie(movies = MockMovieRepository())
        )
    }

    @Test
    fun `Can catch exceptions and deliver with result`() = coroutinesTest {
        val vm = ViewModel(rateMovie = mockk {
            coEvery { this@mockk(any<Movie>(), any<UserRating>()) } answers {
                throw Exception("Something has happened")
            }
        })

        vm[Inception] = Positive
        assert that vm.result.state `is` type<Error>()

        vm.closeChannels()
    }

    @Test
    fun `Show loading while rating`() = coroutinesTest {
        val realRateMovie = RateMovie(MockStatRepository())
        val vm = ViewModel(rateMovie = mockk {
            coEvery { this@mockk(any<Movie>(), any<UserRating>()) } coAnswers {
                delay(500)
                realRateMovie(firstArg(), secondArg())
            }
        })

        vm[Inception] = Positive
        assert that vm.result.state equals Loading
        assert that vm.result.nextData() equals Unit

        vm.closeChannels()
    }

    @Test
    fun `can add to watchlist`() = viewModelTest({
        ViewModel(addMovieToWatchlist = mockAddMovieToWatchlist)
    }) { viewModel ->

        viewModel addToWatchlist Fury
        coVerify { mockAddMovieToWatchlist(Fury) }
    }
}
