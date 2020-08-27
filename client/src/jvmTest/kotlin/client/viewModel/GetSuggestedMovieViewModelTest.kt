package client.viewModel

import assert4k.*
import client.ViewState.Error
import client.ViewState.Success
import domain.DiscoverMovies
import domain.GenerateDiscoverParams
import domain.GetSuggestedMovies
import domain.GetSuggestionData
import domain.MockMovieRepository
import domain.MockStatRepository
import domain.RateMovie
import domain.Test.Movie.DejaVu
import domain.Test.Movie.Inception
import domain.Test.Movie.TheBookOfEli
import domain.Test.Movie.TheGreatDebaters
import entities.Rating
import entities.movies.Movie
import entities.util.TestDispatchersProvider
import entities.util.ViewStateTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.*

internal class GetSuggestedMovieViewModelTest : ViewStateTest() {

    private val stats = MockStatRepository()
    private val rateMovie = RateMovie(stats)
    private fun CoroutineScope.ViewModel(
        randomize: Boolean = true,
        getSuggestedMovies: GetSuggestedMovies = GetSuggestedMovies(
            discover = DiscoverMovies(MockMovieRepository()),
            generateDiscoverParams = GenerateDiscoverParams(randomize = randomize),
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

        // Add some like movies for give more data to the test
        rateMovie(Inception, Rating.Positive)
        rateMovie(TheBookOfEli, Rating.Positive)
        rateMovie(TheGreatDebaters, Rating.Positive)

        val vm = ViewModel(randomize = false)

        assert that vm.result.state `is` type<Success<Movie>>()

        vm.closeChannels()
    }

    @Test
    fun `Skip shown next movie`() = runBlockingTest {

        // Add some like movies for give more data to the test
        rateMovie(DejaVu, Rating.Positive)
        rateMovie(Inception, Rating.Positive)
        rateMovie(TheGreatDebaters, Rating.Positive)

        val vm = ViewModel(randomize = false)

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

        // Add some like movies for give more data to the test
        rateMovie(Inception, Rating.Positive)
        rateMovie(TheBookOfEli, Rating.Positive)
        rateMovie(TheGreatDebaters, Rating.Positive)

        val vm = ViewModel(randomize = false)

        var last: MovieCount? = null

        repeat(100) { count ->
            val current = vm.result.data
            assert that current * {
                it `is not` Null { "Error on iteration #$count" }
                +(this + count) `not equals` last
            }
            last = current + count
            vm.skipCurrent()
        }

        vm.closeChannels()
    }

    @Test
    fun `Does not show liked movies`() = runBlockingTest {

        // Add some like movies for give more data to the test
        rateMovie(TheBookOfEli, Rating.Positive)
        rateMovie(TheGreatDebaters, Rating.Positive)

        val vm = ViewModel(randomize = false)

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

        val vm = ViewModel(randomize = false)

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

    data class MovieCount(val movie: Movie, val count: Int,)
    operator fun Movie?.plus(count: Int) = MovieCount(this!!, count)
}
