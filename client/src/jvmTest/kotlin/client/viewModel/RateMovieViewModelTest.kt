package client.viewModel

import assert4k.*
import client.ViewState.Error
import client.ViewState.Loading
import client.nextData
import domain.MockStatRepository
import domain.RateMovie
import domain.Test.Movie.Inception
import entities.Rating
import entities.Rating.Positive
import entities.movies.Movie
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import util.TestDispatchersProvider
import util.ViewStateTest
import kotlin.test.Test

internal class RateMovieViewModelTest : ViewStateTest() {

    private fun CoroutineScope.ViewModel(
        rateMovie: RateMovie = RateMovie(MockStatRepository())
    ) = RateMovieViewModel(
        this,
        TestDispatchersProvider(),
        rateMovie
    )

    @Test
    fun `Can catch exceptions and deliver with result`() = runBlockingTest {
        val vm = ViewModel(rateMovie = mockk {
            coEvery { this@mockk(any<Movie>(), any<Rating>()) } answers {
                throw Exception("Something has happened")
            }
        })

        vm[Inception] = Positive
        assert that vm.result.state `is` type<Error>()

        vm.closeChannels()
    }

    @Test
    fun `Show loading while rating`() = runBlockingTest {
        val realRateMovie = RateMovie(MockStatRepository())
        val vm = ViewModel(rateMovie = mockk {
            coEvery { this@mockk(any<Movie>(), any<Rating>()) } coAnswers {
                delay(500)
                realRateMovie(firstArg(), secondArg())
            }
        })

        vm[Inception] = Positive
        assert that vm.result.state equals Loading
        assert that vm.result.nextData() equals Unit

        vm.closeChannels()
    }
}
