package client.viewModel

import assert4k.*
import client.ViewState.Error
import client.ViewState.Success
import client.viewModel.GetSuggestedMovieViewModel.Companion.BUFFER_SIZE
import domain.DiscoverMovies
import domain.GetSuggestedMovies
import domain.GetSuggestionData
import domain.MockMovieRepository
import domain.MockStatRepository
import domain.RateMovie
import domain.Test.Movie.TheBookOfEli
import domain.Test.Movie.TheGreatDebaters
import entities.Rating
import entities.movies.Movie
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.TestCoroutineScope
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
        val scope = TestCoroutineScope()
        val vm = scope.ViewModel(getSuggestedMovies = mockk {
            coEvery { this@mockk() } answers {
                throw Exception("Something has happened")
            }
        })

        assert that vm.result.state `is` type<Error>()

        vm.closeChannels()
        scope.cleanupTestCoroutines()
    }

    @Test
    fun `Can deliver suggested movie`() = runBlockingTest {
        val scope = TestCoroutineScope()
        val vm = scope.ViewModel()

        assert that vm.result.state `is` type<Success<Movie>>()

        vm.closeChannels()
        scope.cleanupTestCoroutines()
    }

    @Test
    fun `Does not repeat already liked movies`() = runBlockingTest {

        // Add some like movies for give more data to the test
        rateMovie(TheBookOfEli, Rating.Positive)
        rateMovie(TheGreatDebaters, Rating.Positive)

        val scope = TestCoroutineScope()
        val vm = scope.ViewModel()

        val current = vm.result.data
        assert that current `is` type<Movie>()

        vm.likeCurrent()
        // Skip all the buffer
        repeat(BUFFER_SIZE) { vm.skipCurrent() }

        repeat(BUFFER_SIZE * 10) { count ->
            assert that vm.result.data `not equals` current { "Error on iteration #$count" }
            vm.skipCurrent()
        }

        vm.closeChannels()
        scope.cleanupTestCoroutines()
    }

    @Test
    fun `Does not repeat already disliked movies`() = runBlockingTest {

        // Add some like movies for give more data to the test
        rateMovie(TheBookOfEli, Rating.Positive)
        rateMovie(TheGreatDebaters, Rating.Positive)

        val scope = TestCoroutineScope()
        val vm = scope.ViewModel()

        val current = vm.result.data
        assert that current `is` type<Movie>()

        vm.dislikeCurrent()
        // Skip all the buffer
        repeat(BUFFER_SIZE) { vm.skipCurrent() }

        repeat(BUFFER_SIZE * 10) { count ->
            assert that vm.result.data `not equals` current { "Error on iteration #$count" }
            vm.skipCurrent()
        }

        vm.closeChannels()
        scope.cleanupTestCoroutines()
    }
}
