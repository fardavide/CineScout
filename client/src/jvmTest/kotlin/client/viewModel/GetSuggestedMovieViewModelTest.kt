package client.viewModel

import assert4k.*
import client.TestDispatchersProvider
import client.ViewState.Error
import client.ViewState.Success
import domain.DiscoverMovies
import domain.GetSuggestedMovies
import domain.GetSuggestionData
import domain.MockMovieRepository
import domain.MockStatRepository
import domain.RateMovie
import entities.movies.Movie
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import util.ViewStateTest
import kotlin.test.Test

internal class GetSuggestedMovieViewModelTest : ViewStateTest() {

    private val stats = MockStatRepository()
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
            RateMovie(stats)
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
}
