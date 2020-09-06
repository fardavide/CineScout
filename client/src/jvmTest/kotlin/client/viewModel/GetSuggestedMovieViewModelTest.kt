package client.viewModel

import assert4k.*
import client.ViewState.Error
import client.ViewState.Success
import client.util.ViewModelTest
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
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlin.test.*

internal class GetSuggestedMovieViewModelTest : ViewModelTest {

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
            dispatchers,
            getSuggestedMovies,
            rateMovie
        )
    }

    @Test
    fun `Can catch exceptions and deliver with result`() = coroutinesTest {
        val vm = ViewModel(getSuggestedMovies = mockk {
            coEvery { this@mockk(any<Int>()) } answers {
                throw Exception("Something has happened")
            }
        })

        assert that vm.result.state `is` type<Error>()

        vm.closeChannels()
    }

    @Test
    fun `Can deliver suggested movie`() = viewModelTest({

        // Add some like movies for give more data to the test
        rateMovie(Inception, Rating.Positive)
        rateMovie(TheBookOfEli, Rating.Positive)
        rateMovie(TheGreatDebaters, Rating.Positive)

        ViewModel(randomize = false)

    }) { viewModel ->

        assert that viewModel.result.state `is` type<Success<Movie>>()
    }

    @Test
    fun `Skip shown next movie`() = coroutinesTest {

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
    fun `Does not stop delivering suggestions`() = coroutinesTest {

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
    fun `Does not show liked movies`() = coroutinesTest {

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
    fun `Does not show disliked movies`() = coroutinesTest {

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
