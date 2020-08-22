package client.viewModel

import assert4k.*
import client.ViewState.Error
import client.ViewState.Success
import domain.*
import domain.Test.Movie.TheBookOfEli
import domain.Test.Movie.TheGreatDebaters
import entities.Rating
import entities.movies.Movie
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import util.TestDispatchersProvider
import util.ViewStateTest
import kotlin.test.Test

internal class GetSuggestedMovieViewModelTest : ViewStateTest() {

    private val stats = MockStatRepository()
    private val rateMovie = RateMovie(stats)
    private fun CoroutineScope.ViewModel(
        getSuggestedMovies: GetSuggestedMovies = GetSuggestedMovies(
            discover = DiscoverMovies(MockMovieRepository()),
            getSuggestionsData = GetSuggestionData(stats),
            stats = stats
        ),
    ): GetSuggestedMovieViewModel {
        return GetSuggestedMovieViewModel(
            this,
            TestDispatchersProvider(),
            getSuggestedMovies,
            rateMovie
        )
    }

    @Test
    fun `Can catch exceptions and deliver with result`() = runBlockingTest {
        val vm = ViewModel(getSuggestedMovies = mockk {
            coEvery { this@mockk(any<Int>()) } answers {
                throw Exception("Something has happened")
            }
        })

        assert that vm.result.state `is` type<Error>()

        vm.closeChannels()
    }

    @Test
    fun `Can deliver suggested movie`() = runBlockingTest {
        val vm = ViewModel()

        assert that vm.result.state `is` type<Success<Movie>>()

        vm.closeChannels()
    }

    @Test
    fun `Skip shown next movie`() = runBlockingTest {
        val vm = ViewModel()

        val first = vm.result.state
        assert that first `is` type<Success<Movie>>()

        vm.skipCurrent()
        val second = vm.result.data
        assert that second *{
            it `is not` Null
            it `not equals` first.data
        }

        vm.closeChannels()
    }

    @Test
    fun `Does not stop delivering suggestions`() = runBlockingTest {
        val vm = ViewModel()

        var last: Movie? = null

        repeat(100) {
            val current = vm.result.data
            assert that current * {
                it `is not` Null
                it `not equals` last
            }
            last = current
            vm.skipCurrent()
        }

        vm.closeChannels()
    }

    @Test
    fun `Does not show liked movies`() = runBlockingTest {

        // Add some like movies for give more data to the test
        rateMovie(TheBookOfEli, Rating.Positive)
        rateMovie(TheGreatDebaters, Rating.Positive)

        val vm = ViewModel()

        val current = vm.result.data
        assert that current `is` type<Movie>()

        vm.likeCurrent()

        repeat(100) { count ->
            assert that vm.result.data * {
                it `is not` Null { "Error on iteration #$count" }
                it `not equals` current { "Error on iteration #$count" }
            }
            vm.skipCurrent()
        }

        vm.closeChannels()
    }

    @Test
    fun `Does not show disliked movies`() = runBlockingTest {

        // Add some like movies for give more data to the test
        rateMovie(TheBookOfEli, Rating.Positive)
        rateMovie(TheGreatDebaters, Rating.Positive)

        val vm = ViewModel()

        val current = vm.result.data
        assert that current `is` type<Movie>()

        vm.dislikeCurrent()

        repeat(100) { count ->
            assert that vm.result.data * {
                it `is not` Null { "Error on iteration #$count" }
                it `not equals` current { "Error on iteration #$count" }
            }
            vm.skipCurrent()
        }

        vm.closeChannels()
    }

}
